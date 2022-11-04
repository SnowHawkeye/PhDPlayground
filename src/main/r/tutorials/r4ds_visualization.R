library(ggplot2)
library(dplyr) # for the filter() function

# summary(mpg) # describes the columns

# Template for creating plots:
# ggplot(data = <DATA>) +
#   <GEOM_FUNCTION>(mapping = aes(<MAPPINGS>))

ggplot(data = mpg) + # creates an empty graph
  geom_point(mapping = aes(x = displ, y = hwy, # scatter plot
                           color = class, alpha = displ < 5)) # you can associate different variables with a different aesthetic

# facets are subplots
ggplot(data = mpg) + # creates an empty graph
  geom_point(mapping = aes(x = displ, y = hwy)) +
  facet_wrap(~class, nrow = 2) # the ~ indicates a "formula", which is a data structure in R

ggplot(data = mpg) +
  geom_point(mapping = aes(x = displ, y = hwy)) +
  facet_grid(drv ~ .) # the dot

# different type of plot
ggplot(data = mpg) +
  # geom_point(mapping = aes(x = displ, y = hwy)) + # you can overlap different geoms
  geom_smooth(mapping = aes(x = displ, y = hwy, group = drv))

# a better way to overlap geoms
ggplot(data = mpg, mapping = aes(x = displ, y = hwy)) +
  geom_point(mapping = aes(color = class)) + # this extends the mapping defined for the plot
  geom_smooth(data = filter(mpg, class == "subcompact"), se = FALSE) # you can even change the data; se=false removes the grey area around the smooth curve

# a stat is a computation of the data
ggplot(data = diamonds) +
  geom_bar(mapping = aes(x = cut, fill = clarity), position = "dodge") # stat_count() is the default stat for geom_bar(), but you could define a y or override the default stat
# stat_count(mapping = aes(x = cut)) # equivalent, because geom_bar() is the default geom for stat_count()

# different type of stat
ggplot(data = diamonds) +
  stat_summary(
    mapping = aes(x = cut, y = depth),
    fun.min = min,
    fun.max = max,
    fun = median
  )

# Template for creating more complex plots
# ggplot(data = <DATA>) +
#   <GEOM_FUNCTION>(
#      mapping = aes(<MAPPINGS>),
#      stat = <STAT>,
#      position = <POSITION>
#   ) +
#   <COORDINATE_FUNCTION> +
#   <FACET_FUNCTION>