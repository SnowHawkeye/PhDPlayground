library(ggplot2)
library(dplyr)
library(nycflights13)

# View(flights ) # lets you see a dataset

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

## Adding variables
# a list of useful functions for creating new variables can be found at https://r4ds.had.co.nz/transform.html#mutate-funs

# create a narrower dataset for easier visualisation
flights_sml <- select(flights,
                      year:day, # all columns between year and day
                      ends_with("delay"), # all columns that end with the string "delay"
                      distance,
                      air_time
)

# mutate lets you add new variables
mutate(flights_sml,
       gain = dep_delay - arr_delay,
       speed = distance / air_time * 60,
       hours = air_time / 60,
       gain_per_hour = gain / hours
)

# transmute only keeps the new columns
transmute(flights,
          gain = dep_delay - arr_delay,
          hours = air_time / 60,
          gain_per_hour = gain / hours
)

a <- transmute(flights,
               dep_time,
               hour = dep_time %/% 100, # integer division
               minute = dep_time %% 100 # remainder
)

tail(filter(a, !is.na(hour)), n=5)

## Grouping and summarising rows
# a list of useful summarising functions can be found here https://r4ds.had.co.nz/transform.html#summarise-funs
by_day <- group_by(flights, year, month, day)
summarise(by_day, delay = mean(dep_delay, na.rm = TRUE)) # na.rm removes missing values in the computation

delays <- flights %>% # the %>% ("pipe") lets you call a function on a variable without having to name it
  group_by(dest) %>%
  summarise(
    count = n(),
    dist = mean(distance, na.rm = TRUE),
    delay = mean(arr_delay, na.rm = TRUE)
  ) %>%
  filter(count > 20, dest != "HNL")