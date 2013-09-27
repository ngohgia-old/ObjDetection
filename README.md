ngo.h.gia

This is a simple Android project to "detect" an object from an image captured by a smart phone/ tablet camera. The code used OpenCV library for image processing.

The code basically "detect" objects by selecting area of the same color as where it is touched.

Experimentations:
* Increas the number of times the image is pyrDowned for creating the mask:
- Set pyrDown = 1: the image is too fine-grained. The pixels are too "different" from each other, and therefore, the selected color would be too specific. As the result, the neighbouring colors cannot be selected properly.

- Set pyrDown = 4: the image is too blurred. The similarly colored area got crept up by neighbouring colors. The area selected, therefore, becomes very small.

--> Does not seem to help much
BUG: scaling back the contour by (mPyrDonwScale * 2, mPyrDonwScale * 2) does not seem to work.