library(tidyverse)
library(hms) # has a better date/time parser than base R

# This tutorial gives more detail on specific parameters and on file parsing https://r4ds.had.co.nz/data-import.html

# ⚠️ use read_csv() from the readr package (included in tidyverse), and not read.csv() from base R
read_csv("A line of metadata to skip
a,b,c
1,2,3
# A comment to skip
4,5,6", skip = 1, comment = "#")

read_csv("1,2,3\n4,5,.", col_names = FALSE, na = ".") # default names are given to the columns, you can indicate how NA values are represented in the file
read_csv("1,2,3\n4,5,6", col_names = c("x", "y", "z")) # name columns manually (c() creates a vector)

