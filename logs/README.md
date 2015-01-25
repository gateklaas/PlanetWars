Log files
=========

### Content ###

Automatcally generated files created by the Logger.java.

### Logger ###

The Logger is a competition program designed to test different search algorithms, heristics and stratagies.
The Logger is able to simulate multiple battles between different bots in a sequential order.
After the battles are finished, the Logger saves the results in multiple Comma Separated Values (csv) -files.
For every bot, that participated in a battle, three different csv files are created:

* A file to log the results of the battles when the bot has won
* A file to log the results of the battles when the bot has tied
* A file to log the results of the battles when the bot has lost

Every csv file contains information about the following:

* Opponent bot name
* The map that was played on
* Number of turns the bot has used
* Average time in milliseconds it took to make a turn

### Excel ###

Excel 2013 doesn't always display .csv files properly:
http://stackoverflow.com/questions/17953679/how-to-correctly-display-csv-files-within-excel-2013
