library(ggplot2)
library(dplyr)
library(nycflights13)

filter(flights, year == 2013)
arrange(flights, desc(dep_delay)) # arrange sorts the data

df <- tibble(x = c(5, 2, NA))
arrange(df, desc(is.na(x)))

# Select columns by name
select(flights, year, month, day)

# Select all columns between year and day (inclusive)
select(flights, year:day)

# Select all columns except those from year to day (inclusive)
select(flights, -(year:day))

# The help for the select() function presents helper functions such as "starts_with()" and more