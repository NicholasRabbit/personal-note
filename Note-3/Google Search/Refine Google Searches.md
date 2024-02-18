### 一，Google Search Tips

##### 1) advaced search

https://www.google.com/advanced_search

https://www.freecodecamp.org/news/how-to-google-like-a-pro-10-tips-for-effective-googling/

1. xxx   filetype:pdf  :  to search pdf files
2. site:github.com springboot ：  search for something within a website 
3. xxx  -class : exclude class when you search for something
4. xxx  imagesize:500x600 :  search images of a particular size 
5. \* of Thrones :   use * when you don't remember the word
6. xxx  before:2020  OR  xxx after:2020  OR   xxx  2000 .. 2020 :  search site of particular dates

##### 2) Google  app id

https://github.com/XX-net/XX-Net/wiki/how-to-create-my-appids

##### 3) refine google searches

1.  Exclude words from your search.

   ```shell
   Java framework -spring #Add '-' to a word in order to exclude it.
   ```

2. OR (\|)

   ```shell
   Online course of Java | C++ #To search online course of Java or C++. 
   ```

3. \* : A asterisk. It means wild card in Google search. When using google, the searching result only matches the first ten words of you input. However, you can use asterisk "*", which is not counted by google, to replace some words. 

   ```shell
   * * lady * sure * * glitters * gold * she’s buying * stairway * heaven 
   #You will find exactly what you want from the first line of the searching result.
   #Don't use "There’s a lady who’s sure all that glitters is gold and she’s buying a stairway to heaven".
   ```

4. Similar results.

   ```shell
   ~used ~cars
   ```

5. Exact match

   ```shell
   "unit test" #If you quoted the words, google will match all of them in the results.
   ```

6. Range of numbers

   ```shell
   salary software developer's is $5000..$10000
   ```

7. Operators.

   ```shell
   define:unit test 
   #To search the definitions of "unit test".
   synonym:crucial 
   #The synonyms of a word.
   inurl:unix 
   #Google will return only web pages with your keyword in the URL.
   cache:harpers.org 
   #To find the recent cached version of a web page.
   related:aws.com 
   #To search web pages which are similar to the one you have known of.
   link:harpers.org/archive 
   #To search every website that cites "harpers.org/archive".
   location:USA software developer
   #Results in a specific location.
   ```

   