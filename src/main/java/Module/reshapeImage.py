import numpy as np
import cv2
import matplotlib.pyplot as plt
import java

def reshapeImage(image, width, height):
    # Resize the image
    resized = cv2.resize(image, (width, height), interpolation = cv2.INTER_AREA)
    return resized
