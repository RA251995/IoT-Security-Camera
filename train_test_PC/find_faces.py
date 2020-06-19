from imutils import paths
import cv2
import face_recognition

imgPaths = list(paths.list_images('./dataset/raw_resize/'))
for (i, imgPath) in enumerate(imgPaths):
	print("Processing image {}/{}".format(i + 1,len(imgPaths)))
	img = cv2.imread(imgPath)
	rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
	boxes = face_recognition.face_locations(rgb, model='cnn')
	j = 1;
	for (top, right, bottom, left) in boxes:
		cv2.imwrite('./dataset/faces/'+str(i+1)+'_'+str(j)+'.jpg', img[top:bottom, left:right])
		j+=1
		# cv2.rectangle(img, (left, top), (right, bottom), (0, 255, 0), 2)
#	cv2.imshow('',img)
#	cv2.waitKey(0)
