package com.example.tns.compass;

import java.util.ArrayList;
import java.util.List;

import android.util.FloatMath;
import android.view.Surface;

import com.adamratana.rotationvectorcompass.math.Matrix4;
import com.adamratana.rotationvectorcompass.math.Util;
import com.adamratana.rotationvectorcompass.math.Vector3;
import com.adamratana.rotationvectorcompass.math.Vector4;

/**
 * A really brute-force implementation to derive the orientation of the device
 * based on where the back of the device is pointing when not held flat, or,
 * when held flat, where the top of the device is pointing. This is tricky,
 * because where the device is pointing changes depending on how we're holding
 * the device, as well as the screen rotation of the device.
 * 
 * What this does is rotate all the points according to the rotation matrix, and
 * then take a look at where reference points ended up (6 points on a sphere, to
 * determine bearing, pitch, roll). Yeah, it's bad, and there has to be a much
 * simpler way.
 * 
 * TODO: replace with something that works far more efficiently and reasonably.
 * 
 * @author Adam
 * 
 */
public class OrientationCalculatorImpl implements OrientationCalculator {
	private static final float DEGREES_TO_RADIANS = (float) (Math.PI / 180.0f);
	private static final float RADIANS_TO_DEGREES = (float) (180.0f / Math.PI);

	// determines the number of points of the sphere
	private static final int POINTS_PER_SEGMENT = 72;
	private static final int NUM_SEGMENTS = 11;
	private static final int NUM_POINTS = POINTS_PER_SEGMENT * NUM_SEGMENTS;
	private static final int ORTHO_RESOLUTION = 1000;

	private ArrayList<Vector3> mVertices = new ArrayList<Vector3>(NUM_POINTS + 1);

	private Vector3 mRollTopAbsolute = new Vector3(), mRollBottomAbsolute = new Vector3(), mOriginPoint = new Vector3(), mReticlePoint = new Vector3();
	private Vector3 mSphereTop = new Vector3(), mSphereBottom = new Vector3(), mNorthReference = new Vector3();
	private Vector3 mNorthAbsolute = new Vector3(), mSouthAbsolute = new Vector3(), mWestAbsolute = new Vector3(), mEastAbsolute = new Vector3();

	private Matrix4 mOrthographicProjectionMatrix = new Matrix4();
	private Matrix4 mModelViewMatrix = new Matrix4();

	private List<Vector3> mOrthographicVertexBatch = new ArrayList<Vector3>();
	private Vector4 vTemp = new Vector4();

	public OrientationCalculatorImpl() {
		mOrthographicProjectionMatrix.setToOrtho2D(0, 0, 1, -1);

		for (int i = 0; i < NUM_POINTS; i++) {
			mVertices.add(new Vector3());
		}
		mOrthographicVertexBatch.addAll(mVertices);
		mOrthographicVertexBatch.add(mSphereTop);
		mOrthographicVertexBatch.add(mSphereBottom);
		mOrthographicVertexBatch.add(mNorthReference);
	}

	@Override
	public void getOrientation(Matrix4 rotationMatrix, int screenRotation, float[] out) {
		rotatePoints(rotationMatrix, screenRotation);

		Vector3 neighborPoint;
		float dist = 0;
		float dist2 = 0;
		float closestPointDist = Float.MAX_VALUE;
		float distLR = 0;
		float angle = 0;
		float neighborDist = 0, distToR = 0, distToL = 0;
		float xScale = ORTHO_RESOLUTION;
		float yScale = ORTHO_RESOLUTION;
		float distN, distS, distW, distE;
		float deviceRoll, deviceAltitude, deviceBearing;

		// ALITTUDE
		// altitude - very simple, triangulate between top and bottom point
		mReticlePoint.set(0.5f * xScale, 0.5f * yScale, -xScale);
		mOriginPoint.set(0.5f * xScale, 0.5f * yScale, 0);
		dist = Util.calcDistance(mReticlePoint, mSphereBottom);
		dist2 = Util.calcDistance(mReticlePoint, mSphereTop);
		distToL = Util.calcDistance(mOriginPoint, mSphereBottom);
		distToR = Util.calcDistance(mOriginPoint, mReticlePoint);
		distLR = Util.calcDistance(mSphereBottom, mReticlePoint);
		float altitude = RADIANS_TO_DEGREES * Util.calcAngle(distLR, Util.calcRadius(distToL, distToR, distLR)) - 90;

		// flip quadrant if closer to top of globe
		if (dist > dist2) {
			altitude = -altitude;
		}

		deviceAltitude = altitude;
		if (Float.isNaN(deviceAltitude)) {
			deviceAltitude = 0;
		}

		// BEARING - if held flat, we calculate one way, if not, we calculate
		// another
		mReticlePoint.set(0.5f * xScale, 0.5f * yScale, -xScale);
		if (Math.abs(deviceAltitude) < 75) {
			int closestPoint = 0, pot1 = 0, pot2 = 0, neighbor = 0, left = 0;
			int pointSize = NUM_POINTS;
			// find closest point
			for (int i = 0; i < pointSize; i++) {
				Vector3 v = mVertices.get(i);
				if (v.z < 0) {
					dist = Util.calcDistance(mReticlePoint, v);
					if (dist < closestPointDist) {
						closestPointDist = dist;
						closestPoint = i;
					}
				}
			}

			// potential neighbors for azimuth
			if (closestPoint % POINTS_PER_SEGMENT == 0) {
				pot1 = closestPoint + 1;
				pot2 = closestPoint + POINTS_PER_SEGMENT - 1;
			} else if ((closestPoint + 1) % POINTS_PER_SEGMENT == 0) {
				pot1 = closestPoint - 1;
				pot2 = closestPoint - POINTS_PER_SEGMENT - 1;
			} else {
				pot1 = closestPoint + 1;
				pot2 = closestPoint - 1;
			}

			// bounds check
			if (pot1 >= 0 && pot2 >= 0 && pot1 < pointSize && pot2 < pointSize) {
				dist = Util.calcDistance(mReticlePoint, mVertices.get(pot1));
				dist2 = Util.calcDistance(mReticlePoint, mVertices.get(pot2));

				if (dist < dist2) {
					neighbor = pot1;
					neighborDist = dist;
				} else {
					neighbor = pot2;
					neighborDist = dist2;
				}
				// boundary cases:
				// closest is 345, right is 0
				// closest is 0, left is 345
				if (neighbor < closestPoint) {
					// if we're 345, and point to right is 0, left should be
					// 345,
					// not 0
					if ((closestPoint + 1) % POINTS_PER_SEGMENT == 0 && neighbor == pot2) {
						left = closestPoint;
						distToL = closestPointDist;
						distToR = neighborDist;
					} else {
						left = neighbor;
						distToL = neighborDist;
						distToR = closestPointDist;
					}
				} else {
					if (closestPoint % POINTS_PER_SEGMENT == 0 && neighbor == pot2) {
						left = neighbor;
						distToL = neighborDist;
						distToR = closestPointDist;
					} else {
						left = closestPoint;
						distToL = closestPointDist;
						distToR = neighborDist;
					}
				}
			}

			if (neighbor <= NUM_POINTS - 1 && neighbor >= 0) {
				neighborPoint = mVertices.get(neighbor);
			} else {
				if (neighbor < 0) {
					neighborPoint = mSphereBottom;
				} else {
					neighborPoint = mSphereTop;
				}
			}

			float angleIncrement = (360.0f / POINTS_PER_SEGMENT);
			angle = left % POINTS_PER_SEGMENT * angleIncrement;
			distLR = Util.calcDistance(mVertices.get(closestPoint), neighborPoint);

			deviceBearing = Util.floatrev(angle + angleIncrement * (((float)Math.cos(Util.calcAngleClamp(distToR, Util.calcRadius(distToL, distToR, distLR))) * distToL) / distLR) - 180);
		} else {
			// calc current N Point distance from original compass points
			mNorthAbsolute.set(0.5f * xScale, 0.2f * xScale + (yScale - xScale) / 2, 0);
			mSouthAbsolute.set(0.5f * xScale, 0.8f * xScale + (yScale - xScale) / 2, 0);
			mWestAbsolute.set(0.2f * xScale, 0.5f * yScale, 0);
			mEastAbsolute.set(0.8f * xScale, 0.5f * yScale, 0);

			distN = Util.calcDistance(mNorthReference, mNorthAbsolute);
			distS = Util.calcDistance(mNorthReference, mSouthAbsolute);
			distW = Util.calcDistance(mNorthReference, mWestAbsolute);
			distE = Util.calcDistance(mNorthReference, mEastAbsolute);

			distToL = Util.calcDistance(mOriginPoint, mNorthReference);
			distToR = Util.calcDistance(mOriginPoint, mNorthAbsolute);
			distLR = Util.calcDistance(mNorthReference, mNorthAbsolute);

			float bearing = RADIANS_TO_DEGREES * -Util.calcAngle(distLR, Util.calcRadius(distToL, distToR, distLR));

			if (distN < distS) {
				if (distW < distE) {
					bearing = 360 - bearing;
				}
			} else {
				if (distW < distE) {
					bearing += 180;
				} else {
					bearing = 180 - bearing;
				}
			}

			if (deviceAltitude > 0) {
				bearing = 180 - bearing;
			}
			deviceBearing = Util.floatrev(bearing);
		}

		if (Float.isNaN(deviceBearing)) {
			deviceBearing = 0;
		}

		// ROLL - calculate only when not held flat, ignore when altitude
		// (pitch) is less than 15
		if (Math.abs(deviceAltitude) < 75) {
			mRollTopAbsolute.set(0.5f * xScale, 0.2f * yScale, 0);
			mRollBottomAbsolute.set(0.5f * xScale, 0.8f * yScale, 0);

			Vector3 upDown;
			Vector3 topBot;
			boolean altAbove = true;

			if (deviceAltitude < 0) {
				altAbove = false;
				upDown = mRollBottomAbsolute;
				topBot = mSphereTop;
			} else {
				upDown = mRollTopAbsolute;
				topBot = mSphereBottom;
			}

			float adist = (float)Math.sqrt((upDown.x - mOriginPoint.x) * (upDown.x - mOriginPoint.x) + (upDown.y - mOriginPoint.y) * (upDown.y - mOriginPoint.y));
			float bdist = (float)Math.sqrt((upDown.x - topBot.x) * (upDown.x - topBot.x) + (upDown.y - topBot.y) * (upDown.y - topBot.y));
			float cdist = (float)Math.sqrt((mOriginPoint.x - topBot.x) * (mOriginPoint.x - topBot.x) + (mOriginPoint.y - topBot.y) * (mOriginPoint.y - topBot.y));

			float val = ((adist * adist) + (cdist * cdist) - (bdist * bdist)) / (2 * adist * cdist);
			if (val < 1) {
				float atheta = (float) Math.acos(((adist * adist) + (cdist * cdist) - (bdist * bdist)) / (2 * adist * cdist)) * RADIANS_TO_DEGREES;
				if (altAbove) {
					if (upDown.x - topBot.x < 0) {
						atheta = -atheta;
					}
				} else {
					if (upDown.x - topBot.x > 0) {
						atheta = -atheta;
					}
				}
				atheta = Util.floatrev(atheta);
				// restrict the roll to increments of 0.5 degrees - we don't
				// need the precision here, also steady within 3 degrees
				if (atheta <= 3 || atheta >= 357) {
					atheta = 0;
				} else {
					atheta = 0.5f * Math.round(atheta / 0.5);
				}
				deviceRoll = atheta;
			} else {
				deviceRoll = 0;
			}
		} else {
			deviceRoll = 0;
		}
		if (Float.isNaN(deviceRoll)) {
			deviceRoll = 0;
		}
		out[0] = deviceBearing;
		out[1] = deviceAltitude;
		out[2] = deviceRoll;
	}

	private void resetPoints() {
		mSphereTop.set(0, 0, 1);
		mSphereBottom.set(0, 0, -1);

		for (int j = 0; j < NUM_SEGMENTS; j++) {
			int idx = j - 5;
			float jCosVal = (float)Math.cos(DEGREES_TO_RADIANS * (float) (idx * 15));
			float jCosValInv = (float)Math.cos(DEGREES_TO_RADIANS * (float) (90 - idx * 15));
			for (int i = 0; i < POINTS_PER_SEGMENT; i++) {
				float sinVal = (float)Math.sin(DEGREES_TO_RADIANS * (float) (i * (360 / POINTS_PER_SEGMENT)));
				float cosVal = (float)Math.cos(DEGREES_TO_RADIANS * (float) (i * (360 / POINTS_PER_SEGMENT)));
				mVertices.get(i + (POINTS_PER_SEGMENT * j)).set(sinVal * jCosVal * 1, -cosVal * jCosVal * 1, jCosValInv * 1);
			}
		}

		// a reference to the N point on the sphere.
		mNorthReference.set(mVertices.get(POINTS_PER_SEGMENT * 5));
	}

	/**
	 * With rotationMatrix, rotate the view components
	 * 
	 * @param rotationMatrix
	 */
	public void rotatePoints(Matrix4 rotationMatrix, int screenRotation) {
		resetPoints();
		final int width = ORTHO_RESOLUTION;
		final int height = ORTHO_RESOLUTION;
		final float orthoScale = 1.0f;
		mModelViewMatrix.idt().mul(mOrthographicProjectionMatrix).mul(rotationMatrix);
		switch (screenRotation) {
		case Surface.ROTATION_0:
		case Surface.ROTATION_90:
		case Surface.ROTATION_270:
			for (Vector3 v : mOrthographicVertexBatch) {
				vTemp.set(v.x, -v.y, -v.z, 0);
				vTemp.mul(mModelViewMatrix);
				v.x = (vTemp.x) * 0.5f * width * orthoScale + width / 2;
				v.y = (vTemp.y) * 0.5f * width * orthoScale + width / 2 + (height - width) / 2;
				v.z = vTemp.z * 0.5f * width * orthoScale;
			}
			break;
		// For 180, we have to reflect x and y values
		case Surface.ROTATION_180:
			for (Vector3 v : mOrthographicVertexBatch) {
				vTemp.set(v.x, -v.y, -v.z, 0);
				vTemp.mul(mModelViewMatrix);
				// reflect x and y axes...
				v.x = (-vTemp.x) * 0.5f * width + width / 2;
				v.y = (-vTemp.y) * 0.5f * width + width / 2 + (height - width) / 2;
				v.z = vTemp.z * 0.5f * width;
			}
			break;
		}
	}
}