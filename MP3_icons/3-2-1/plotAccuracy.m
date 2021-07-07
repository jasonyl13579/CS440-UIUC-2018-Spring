clear all; clc; close all;

TrainM = textread('TrainingAccuracy.txt');
figure;
plot(1:size(TrainM), TrainM); title('Training Accuracy vs Epochs');
axis([1 inf 0.9 1]); xlabel('Epochs');ylabel('Accuracy');

%TestM = textread('TestingAccuracy.txt');
%figure;
%plot(1:size(TestM), TestM); title('Testing Accuracy vs Times');
%axis([1 inf 0.9 1]); xlabel('Times');ylabel('Accuracy');
