# hand_written_digits_recognition
General Description 
The purpose of this project is to implement various classifiers for hand-written digit recognition.
The project consists of multiple components that involve using different representations for the dataset, 
implementing three different classifier models, assessing the performance of each classifier with the 
different representations, and analyzing some of the models that were estimated.

Dataset 
The dataset is derived from the MNIST dataset. About the MNIST, detail information can be found at: 
http://yann.lecun.com/exdb/mnist/
The input dataset is equally split into three subsets which are used to serve as training set, validation set as 
well as testing set.
In adddition, three different versions of datasets are generated from the original dataset which are specified as 
rep1, rep2 and rep3

Classification Approach
The project applies two different algorithms which are:
- knn (k-nearest neighbors) classifier
- Ridge regression classifier

For Knn, the program uses the validation set to measure the performance of different values of K in the range of 1-20, 
and select the value of K that achieves the best performance. Then it will combine the train and validation set into 
a larger train set and use the selected value of K to classify the test set and measure its performance.

For RR, since there are 10 classes, the program sets up 10 one-vs-rest binary classifiers, in which the +ve class is encoded 
as +1 and the -ve class is encoded as -1. By using these 10 binary models, an instance will be assigned to the class whose 
corresponding one-vs-rest binary model results in the highest prediction value (i.e., use an argmax approach). 
The program will then use the train set to estimate different models for the following values of 
lambda: {.01, 0.05, 0.1, 0.5, 1.0, 2.0, 5.0}. The validation set will be used to evaluate the performance of these models, 
and the lambda value that achieves the best performance will be selected. Then the program will combine both the train 
and validation set and estimate two sets of new models using the following values of lambda: 
- The lambda selected previously
- A value of lambda that is two times greater than the previously selected lambda value
These models will be used to classify the test set and measure their performance.

