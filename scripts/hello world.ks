# A simple hello world in different ways
#
# Author: Konloch
#

memory	10

store	0	"Hello World"
store	1	"World"
store	2	"Hello"
store	3	"Hel"
store	4	"Wor"

print	"Hello World"
print	memory0
print	memory2_+_" "_+_memory1
print	memory3_+_"lo "_+_memory4_+_"ld"