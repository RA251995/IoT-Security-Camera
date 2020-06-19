# INTERNET ENABLED SECURITY SYSTEM

<img src="img/IoT_Presentation0.jpg" width=500px />

# AIM OF THE PROJECT

To develop a camera enabled security system having both local autonomous and remote manual control capabilities\.

# FEATURES

* Livecamerafeed streaming
* Auto unlock with face identification
* Remote control using Android application
  * Access control
  * Access logging
  * Camera orientation control

# MAIN HARDWARE COMPONENTS

<img src="img/IoT_Presentation1.jpg" width=500px />

* Raspberry Pi
  * __SoC__  __:__ Broadcom BCM28373 Model B
  * __CPU__  __:__ 1\.2GHz Quad\-coreARMCortex\-A53                                                              \(50% faster than Raspberry Pi 2\)
  * __GPU:__ BroadcomVideoCoreIV
  * __RAM:__ 1GB LPDDR2 \(900MHz\)
  * __GPIO:__ 40\-pinheader
  * __Networking__  __:__
    * 10/100 Ethernet
    * 2\.4GHz802\.11nWiFi
  * __Bluetooth:__ Bluetooth 4\.1Low Energy
  * __Ports:__ HDMI\, Audio\-videojack\,4USB 2\.0\,Ethernet\, CameraSerial Interface \(CSI\)\, Display Serial Interface \(DSI\)

<img src="img/IoT_Presentation2.jpg" width=500px />

* Raspberry Pi Camera Rev 1\.3
  * __Still resolution__ : 5 Megapixels
  * __Video modes__ :1080p @ 30 fps\, 720p @ 60 fpsand640×480 @ p60/90 fps
  * Plugs into CSI Connector of RaspberryPi
  * __Size__ :Around 25 × 24 × 9 mm
  * __Weight__ : 3g

<img src="img/IoT_Presentation3.jpg" width=500px />

* Pan & Tilt Module
  * 2 x SG90 servo
    * __Operating__  __Voltage__ : \+5V
    * __Torque__ : 2\.5kg/cm
    * __Operating__  __speed__ : 0\.1s/60°
    * __Gear Type__ : Plastic
    * __Rotation__ :0°\- 180°
    * __Weight__ :9gm

# SYSTEM OVERVIEW

<img src="img/IoT_Presentation4.png" width=500px />

<img src="img/IoT_Presentation5.png" width=500px />

<img src="img/IoT_Presentation6.png" width=500px />

LIVE CAMERA STREAMING

__PUSHY CLOUD SERVER__

<img src="img/IoT_Presentation7.jpg" width=462px />

__ANDROID__

__APPLICATION__

<img src="img/IoT_Presentation8.png" width=500px />

<img src="img/IoT_Presentation9.jpg" width=500px />

<img src="img/IoT_Presentation10.jpg" width=500px />

<img src="img/IoT_Presentation11.png" width=500px />

ELECTRIC

LOCK

\( _LED USED FOR_

_PROTOTYPING_ \)

PAN & TILT

MODULE

<img src="img/IoT_Presentation12.png" width=384px />

# LIVE STREAMING

  * Using Raspberry Camera & UV4L streamer software
  * _U_ ser space _V_ ideo _4L_ inux
    * __User space__ driversfor real or virtual video input and outputdevices
    * Includes genericpurpose _Streaming Server_ plug\-in\, especially made forIoTdevices\.

# FACE IDENTIFICATION ALGORITHM

Get image from the camera

Detect faces in the image usingHaarCascade Classifier

Quantify each face to construct128\-dembeddingsusingdlibneural network

Classify face using k\-NearestNeighbourmethod

# FACE IDENTIFICATION ALGORITHMHAAR CASCADE CLASSIFIER

Machinelearning based approach where a cascade function is trained from a lot of positive and negative images\. It is then used to detect objectsin images\.

HAAR CASCADE CLASSIFER

# FACE IDENTIFICATION ALGORITHMCREATING FACE EMBEDDINGS

Quantify faces into 128\-d embedding usingdlibpre\-trained neural network\.

The neural network has been trainedona dataset of ~3 millionimages using deep\-metriclearning \(99\.38% accuracy\)\.

<img src="img/IoT_Presentation15.png" width=500px />

# FACE IDENTIFICATION ALGORITHMK-NEAREST NEIGHBOUR CLASSIFICATION

Find Euclidean distance between theembeddingsand already storedembeddingsof known faces\.

Identify the face based on minimum distance\.

<img src="img/IoT_Presentation17.png" width=500px />

<span style="color:#FF0000"> __Row with minimum distance__ </span>

# FACE IDENTIFICATION ALGORITHMSUMMARY

HAAR CASCADE CLASSIFER

# ACCESS CONTROL & LOGGING

IF FACE IDENTIFIED?

LOG ACTIVITY &

SEND ACCESS REQUEST TO ADMIN MOBILE

LOG ACTIVITY &

UNLOCK DOOR

IF ACCESS GRANTED?

SEND ACCESS GRANTED MESSAGE TO ADMIN

# ACCESS CONTROLPUSH NOTIFICATIONS

<img src="img/IoT_Presentation21.png" width=500px />

Pushy Cloud Messaging Service

Offerscross\-platformreliablenotification delivery

Based on light\-weight MQTT protocol

<img src="img/IoT_Presentation22.png" width=483px />

# ACCESS CONTROLPUSH NOTIFICATIONS BACKEND

<img src="img/IoT_Presentation23.png" width=500px />

Raspberry Pi

API HTTP POST Request using Python

* __API__  __Endpoint__
* POSThttps://api\.pushy\.me/push?api\_key= __SECRET\_API\_KEY__
* __Request__  __Headers__
* Content\-type:application/json
* __JSON Payload__
  * \{
  * 'data': \{
  * 'message' : __MESSAGE__ \,
  * 'username' : __USERNAME__
  * \}\,
  * 'to': '/topics/ __BROADCAST\_TOPIC__ '
  * \}

# ACCESS CONTROLPUSH NOTIFICATIONS CLIENT

<img src="img/IoT_Presentation24.png" width=500px />

Android Application

Android __BroadcastReceiver__ Service  running in the background

# ACCESS LOGGING

<img src="img/IoT_Presentation25.png" width=500px />

Access history saved to a file in Raspberry Pi

File hosted on Apache HTTP Server running on Raspberry Pi

Accessed remotely by Android Application

<img src="img/IoT_Presentation26.jpg" width=500px />

# CAMERA ORIENTATION CONTROL

Using 2 SG90 servo motors attached to pan & tilt module

Connected to Pi PWM pins

Controlled remotely using Android Application

GET Request send to Pi web server based on user input \(← / → / ↑ / ↓ \)

Handled by PHP script controlling Pi PWM duty cycle

# CAMERA ORIENTATION CONTROLDC SERVO MOTOR

PWM to Voltage Converter

Position Sensor

\(Potentiometer\)

<img src="img/IoT_Presentation27.png" width=500px />

<img src="img/IoT_Presentation28.png" width=379px />

<img src="img/IoT_Presentation29.png" width=379px />

<img src="img/IoT_Presentation30.png" width=500px />

<img src="img/IoT_Presentation31.png" width=500px />

<img src="img/IoT_Presentation32.png" width=379px />

# IMPROVEMENTS FOR THE PROJECT

Real\-time streaming FPS can be improved by usingWebRTCprotocol

Camera settings can be adjusted automatically based on time and light based to improve visibility

Provision to add and remove authenticated users can be added

Centroid tracking can be included to track objects

Android application can be ported to other platforms

# SCOPE OF THE PROJECT

Face identification based attendance system

Multiple security cameras can be used for tracking a specified object by including M2M technology

