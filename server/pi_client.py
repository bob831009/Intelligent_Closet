import socket, sys
from string import *
import RPi.GPIO as GPIO
import time

port = atoi(sys.argv[2])
#GPIO.setmode(GPIO.BOARD)
#GPIO.cleanup()
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)

table = [7, 11, 12, 13, 15, 16, 18, 22]
for i in table:
    GPIO.setup(i, GPIO.OUT)
    GPIO.output(i, GPIO.LOW)

try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
except socket.error, msg:
    sys.stderr.write("[ERROR] %s\n" % msg[1])
    sys.exit(1)

try:
    sock.connect((sys.argv[1], port))
except socket.error, msg:
    sys.stderr.write("[ERROR] %s\n" % msg[1])
    exit(1)

time.sleep(3)
for i in table:
    GPIO.output(i, GPIO.HIGH)

tmp = ["7", "7"]
while True:
    try:
        message = sock.recv(1024)
    except KeyboardInterrupt:
        for i in table:
            GPIO.output(i, GPIO.HIGH)
        GPIO.cleanup()
        sys.exit()

    if len(message.split('_')) < 3:
        continue
    GPIO.output(table[atoi(tmp[0])], GPIO.HIGH)
    GPIO.output(table[atoi(tmp[1])], GPIO.HIGH)
    tmp = message.split('_')
    print "Hanger " + tmp[0] + " and " + tmp[1] + " is now flashing."
    GPIO.output(table[atoi(tmp[0])], GPIO.LOW)
    GPIO.output(table[atoi(tmp[1])], GPIO.LOW)
sock.close()
GPIO.cleanup()

