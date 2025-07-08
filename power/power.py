'''
计算power是为了得到是否正在积蓄能量，或者是已经走到了尽头。
计算power过程中，需要留意的几个点：
1）归一化：P和D都要归一化，P的归一化=P/P_PRE，D的归一化=D/D_TOTAL，其中P_PRE和D_TOTAL都可以通过数据库操作查询得到；
2）POWER累积的时间跨度不易过长：时间跨度太久的Power积累没有意义，因为在当下技术流派中，一般都是“开始时参与，结束时撤退”，没有提前埋伏的习惯，拼的是手速；
3）在第一步统计DAY20的sum Power以后，对筛选出来的targets再次谨慎计算其power时，一定要把P的爬升考虑进去。Power是动能，P的爬升代表势能的累积。当累积动能趋近于势能时，是特别需要留意的。
   U = mgh
   m：1-sum(D_DAY)，h=P_CUR/P_PRE_20，g = 1.5
   其实更多时候就是要解决POWER这个动能和势能之间的平衡点，当势能达到一个点，而动能的增长无法对抗势能的增长时，就是POWER失效的时候。
4）每个个体的POWER的累积可能意义不是很大，或者说个体的POWER可能只是偶然因素导致，这样的POWER能量较弱，很快就会被抹平，非我所指。我们需要POWER的增长更可持续，就不能只追求个体的power，而应该去找群体的power。而群体
   如果存在很多个个体有一致行为，那可以在计算power时加上一个T(team)系数，T系数代表着行业的期望值。
5）在计算power时，不可以只计算一个总和，需要将Postive和Negtive的两种power分别计算，这样方便判断当前状态。随着时间的推移，两种power都会增长。良性的推演方式可能是：
   p1）Postive的power较大，negtive的power较小；P/N > 1.2
   p2）Postive的power持续变大，negtive的power也增大；P/N > 1
   p3）Postive的power逐渐趋于平衡，negtive的power越来越大； P/N = 1
   p4）Negtive的power大于Postive的power；结束
6）在临界点上，因为Postive已经达到极限，只能被动化解Negtive的输出，所以在临界点上应该把临界点上的输出作为Postive和Negtive的双向输出。
7）以LV2数据为基础，增量展示20天的power交织情况。
'''


import numpy as np
import math

magic_number_t = 3600*4

magic_number_b_0 = 10
magic_number_b_1 = 20
magic_number_b_2 = -10
magic_number_b_3 = -20

o = np.array([[-1, -1.00], [-2, -1.01], [-3, -1.01], [6, -1.01], [4, -1.00]])

def power_of_pair(a,b):
    #TODO:how to judge the action of (a,b) to be B/S
    # if b === magic_number_b_*, the B/S may be inverted
    return a*math.exp(abs(b))

def sum_of_power(arr):
    sum_postive_power = 0
    sum_negtive_power = 0
    for row in arr:
        a = row[0]
        b = row[1]
        if a > 0:
            sum_postive_power += power_of_pair(a,b)
        else:
            sum_negtive_power += power_of_pair(a,b)

    print(sum_postive_power, sum_negtive_power)

sum_of_power(o)
