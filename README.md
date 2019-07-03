# Chatbot

Introduction:-
In this project I made a chatbot which can interact or chat with humans without any human support. All information related to this work has
been embedded into chatbot database, so it can identify the sentence and generate a desired outcome. For sentence identification input
sentence will be divided into array of words and after matching the sentence with stored sentences chatbot will find a perfect match with the help of extraction algorithm. To improve the performance I added a sentiment analysis so we can focus on more positive replay.

Architecture:-
https://github.com/AbdeAliSomesar/Chatbot/blob/master/Architecture%20flowchart.png
https://github.com/AbdeAliSomesar/Chatbot/blob/master/Sentiment%20analysis%20flowchart.png

Application Screenshort:-https://github.com/AbdeAliSomesar/Chatbot/blob/master/App%20Screenshort%20.png

Workflow:-
To achieve all this functionality I divided the work in to four phases in first three phases chatbot will try to find match for replay and in
fourth phases chatbot select the replay on based on sentiments score of each selected sentence and replay with the top rated sentence.

->In first phase chat bot try to find the match in first dataset in which pre-selected set of questions and answer are defined. Questions
like who are you? How are you? Do you know me? Are you robot? Etc. this questions are pretty common, so I put them in a separate dataset.
Chatbot will match the whole sentence with these sentences for better quality of replay. If match not found in first phase then we move
towards second phase.

->In second phase chatbot look into another dataset which is collected from movies. This data set has thousands of conversations in it.
Chatbot will match the whole sentence in dataset line by line and if match found in dataset then chatbot extract those lines with their
line Id. Then chatbot will find the replay of that line with help of line id and go to fourth phase. If input not matches with any data set
then chatbot will go to third phase.

->In third phase will use the same data set it used in second phase but this time chatbot will not search the whole sentence, it will perform
segmentation of input and match that individual words in data set line by line, when a line find more than 70% matches will be selected for
further process. After selection of lines which score more than 70% go into another process in which we add more scores to it on bases of
their length and their previous score like if a line got more than 80% or more then 90% then we add more values to its score. After all
this process chatbot will sort the lines on base of their score line selection chatbot will sort lines in descending order on the base of
their score.  After that chabot will check if selected lines have replays or not if replay of first sorted line  found then it will check 
score of second line and subtract it from first line score if deference is not big it will select the replay of second line and again check
for deference between first and third line and so on. After all this chatbot will go to fourth phase.

->In fourth phase chatbot collect all selected lines and does sentiment analysis on it and after that select the top most scored line and
sent it as a replay to user. For sentiment analysis chatbot first select lines one by one and made its partitions or we can say divide it
into words. Then it will compare words in dataset of sentiment analysis and if it finds a match then label (positive or negative) that word
and search for another match and so on. After labeling chatbot will sum them up and return the overall score of that particular line. Alter
labeling all lines chatbot will find the top rated line and return it as a replay to user.

