
import time
"""level 2 """
"""lexiographical indexes"""

def solution(list,target):
    a=time.time_ns()

    i=co1=co2=co3=0

    if target > sum(list):
        return "{},{} count1={} count2={} time={}".format(-1,-1,co1,co2,time.time_ns()-a)
    while i < len(list)-1:
        j=i
        s=0

        co1=co1+1
        while s < target and j < len(list):
            s = list[j]+s
            j=j+1

            co2=co2+1
            if s == target:
                return "{},{} count={} count={} time={}".format(i,j-1,co1,co2,time.time_ns()-a)
        i=i+1
    return "{},{} count={} count={} time={}".format(-1,-1,co1,co2,time.time_ns()-a)

list1 = [0 for i in range(1,100)]
a =solution(list1,1)
print(a)

























# def solution(l,t):
#     i=0
#     if t > sum(l):
#         return [-1,-1]
#     while i < len(l)-1:
#         j=i
#         s=0
#         while s < t and j < len(l):
#             s = l[j]+s
#             j=j+1
#             if s == t:
#                 return [i , j-1]
#         i=i+1
#     return [-1 ,-1]
#
# a =solution([0,0,0,12,0],16)
# print(a)
