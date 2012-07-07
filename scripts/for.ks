# A simple for test
#
# Author: Konloch
#

memory	1

store	0	memory0_+_"1"
print	memory0
ifeq	memory0	"1111111"	11	13
print	"Good job, it went to "_+_memory0
goto	15
goto	8

memory	1
store	0	"0"
store	0	memory0_++_"1"
print	memory0
ifeq	memory0	"5"	20	22
print	"Good job, it went to "_+_memory0
end
goto	17