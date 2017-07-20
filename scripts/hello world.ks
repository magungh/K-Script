function f (int [5,7,8,3] arr) returns int
  int i = 0
  int s = 0
  while i < length (arr)
    s = s + arr[i]
    i = i + 1
  end
  return s
end
