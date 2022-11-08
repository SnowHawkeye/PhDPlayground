library(tidyverse)

# Tibbles are modern dataframes
# They print more ergonomically
# They do not do partial matching and give out a warning when trying to access a column that does not exist

# They can be created from regular dataframes
as.tibble(iris)
# The opposite is possible with
# class(as.data.frame(tb))

# Or from new vectors
tibble(
  x = 1:5,
  y = 1,
  z = x^2 + y, # variables that were just created can be referenced
  `:)` = "smile" # backticks allow the use of special characters
)

tribble( # transposed tibble
  ~x, ~y, ~z, # column heading are defined by formulas i.e. start with ~
  # --|--|--
  "a", 2, 3.6,
  "b", 1, 8.5
)

df <- tibble(
  x = runif(5),
  y = rnorm(5)
)

df

# Extract column by name (all of the following do the same)
df$x
df %>% .$x # the dot is a placeholder, necessary to use the pipe here
df[["x"]]
df %>% .[["x"]]

# Extract column by position (the $ cannot extract by position)
df[[1]]