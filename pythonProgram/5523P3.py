import matplotlib
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.image as mpimg

def main():
    fileName = input("Name: ")
    fin = open(fileNamw,'r')
    imageSet = []
    for line in fin:
        tempLis = line.split(", ")
        storeLis = []
        counter = 0
        while (counter < 28):
            tempLis2 = tempLis[(counter*28+0):(counter*28+28)]
            tempLis3 = []
            for item in tempLis2:
                tempLis3.append(int(item))
            storeLis.append(tempLis3)
            counter = counter +1
        image = np.array(storeLis)
        plt.imshow(image)
    return 0

if __name__ == "__main()__":
    main()
            
            
