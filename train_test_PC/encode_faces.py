from imutils import paths
import os
import cv2
import face_recognition
import pickle

imgPaths = list(paths.list_images('./dataset/final/'))
knownEncodings = []
knownNames = []
for (i, imgPath) in enumerate(imgPaths):
	print("Processing image {}/{}".format(i + 1,len(imgPaths)))
	name = imgPath.split(os.path.sep)[-2]
	img = cv2.imread(imgPath)
	h, w, _ = img.shape
	rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
	# compute the facial embedding for the face
	encodings = face_recognition.face_encodings(rgb,[(0,w,h,0)])
	# loop over the encodings
	for encoding in encodings:
		# add each encoding + name to our set of known names and encodings
		knownEncodings.append(encoding)
		knownNames.append(name)
print("Serializing encodings...")
data = {"encodings": knownEncodings, "names": knownNames}
f = open("encodings.pickle", "wb")
f.write(pickle.dumps(data))
f.close()
