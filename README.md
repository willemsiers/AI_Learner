Naive bayesian classifier and an interactive machine learning program.
==========

A project with as goal to build a naive bayesian classifier and an interactive learner, for an AI course in module 6: Intelligent Interaction. 

***Run instructions:*** (tested with java 1.7.0_72)

From project root run: 

To generate a compilation output directory: 

<code>mkdir bin  </code> 

Compile java program: 

<code>javac -d ".\bin" -cp ".;libs/miglayout-4.0-swing.jar;libs/weka.jar" src/mod6/*.java</code>

Run the main Gui for the interactive learner: 

<code>java -cp "bin;libs/miglayout-4.0-swing.jar;libs/weka.jar" mod6.GuiMain </code>

For other runnable classes (such as a performance test of the classifier), see the source code.
 
(A short demonstration and an explaination is provided in the pdf files in the docs folder)