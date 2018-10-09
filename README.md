# evo_comp_43

#### Run current code with:

javac -cp contest.jar player43.java Recombination.java Mutation.java Individual.java

 jar cmf MainClass.txt submission.jar player43.class Individual.class Recombination.class Mutation.class SimpleArithRecombination.class UniformMutation.class etc (add all classes used)

 java -jar testrun.jar -submission=player43 -evaluation=SphereEvaluation -seed=1

## Bunch running
If you want to run the code with different parameters, run the bash file test, and uncomment the parameter imput code in player43.java. It is recommended to run the following: ./test > testedFuncion_timestamp.txt, that way the output will be saved on a txt file.