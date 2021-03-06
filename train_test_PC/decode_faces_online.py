import urllib.request
import numpy as np
import pickle
import cv2
import imutils
import face_recognition

url='http://<ip_camera_address>/stream/snapshot.jpeg'

print("Loading encodings & face detector...")
data = pickle.loads(open('encodings.pickle', "rb").read())
detector = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

while True:
	# Use urllib to get the image from the IP camera
	imgResp = urllib.request.urlopen(url)
	# Numpy to convert into a array
	imgNp = np.array(bytearray(imgResp.read()),dtype=np.uint8)
	# Finally decode the array to OpenCV usable format ;) 
	frame = cv2.imdecode(imgNp,-1)

	frame = imutils.resize(frame, width=500)

	# convert the input frame from (1) BGR to grayscale (for face detection) and (2) from BGR to RGB (for face recognition)
	gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
	rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
	 
	# detect faces in the grayscale frame
	rects = detector.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))
	 
	# OpenCV returns bounding box coordinates in (x, y, w, h) order but we need them in (top, right, bottom, left) order, so we need to do a bit of reordering
	boxes = [(y, x + w, y + h, x) for (x, y, w, h) in rects]

	# compute the facial embeddings for each face bounding box
	encodings = face_recognition.face_encodings(rgb, boxes)
	names = []

	# loop over the facial embeddings
	for encoding in encodings:
		# attempt to match each face in the input image to our known encodings
		matches = face_recognition.compare_faces(data["encodings"], encoding)
		name = "Unknown"
	 
		# check to see if we have found a match
		if True in matches:
			# find the indexes of all matched faces then initialize adictionary to count the total number of times each face was matched
			matchedIdxs = [i for (i, b) in enumerate(matches) if b]
			counts = {}
	 
			# loop over the matched indexes and maintain a count for each recognized face face
			for i in matchedIdxs:
				name = data["names"][i]
				counts[name] = counts.get(name, 0) + 1
	 
			# determine the recognized face with the largest number of votes (note: in the event of an unlikely tie Python will select first entry in the dictionary)
			name = max(counts, key=counts.get)
			
			# update the list of names
		names.append(name)

	# loop over the recognized faces
	for ((top, right, bottom, left), name) in zip(boxes, names):
		# draw the predicted face name on the image
		cv2.rectangle(frame, (left, top), (right, bottom), (0, 255, 0), 2)
		y = top - 15 if top - 15 > 15 else top + 15
		cv2.putText(frame, name, (left, y), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 255, 0), 2)
		print(name)
	 
	# display the image to our screen
	cv2.imshow("Frame", frame)
	# Quit if q is pressed
	if cv2.waitKey(1) & 0xFF == ord('q'):
		break


