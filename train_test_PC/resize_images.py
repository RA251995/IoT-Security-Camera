from imutils import paths
import cv2

imgPaths = list(paths.list_images('./dataset/raw/'))
for (i, imgPath) in enumerate(imgPaths):
	print("Processing image {}/{}".format(i + 1,len(imgPaths)))
	img = cv2.imread(imgPath)
	img_new = cv2.resize(img, None, fx=0.2, fy=0.2)
	cv2.imwrite('./dataset/raw_resize/'+str(i+1)+'.jpg',img_new)

