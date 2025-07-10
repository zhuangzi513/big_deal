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

import pandas as pd
import numpy as np
import math

columns_name = []
GLOBAL_PRE_CLOSE = 4.61

def power_of_pair(n, p):
    #TODO:how to judge the action of (a,b) to be B/S
    # if b === magic_number_b_*, the B/S may be inverted
    if p > GLOBAL_PRE_CLOSE:
        diff = (p - GLOBAL_PRE_CLOSE)/GLOBAL_PRE_CLOSE
    else:
        diff = (GLOBAL_PRE_CLOSE - p)/GLOBAL_PRE_CLOSE
    return n*math.exp(1+abs(diff))

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


# o_DN, o_PW = compute_power_of(SUM_DN_ARR, SUM_DP_ARR, BS_PRE)
def compute_power_of(dn_ARR, dp_ARR, _BS):
    o_PW = 0.0
    o_DN = 0
    for item1, item2 in zip(dn_ARR, dp_ARR):
        o_PW += power_of_pair(item1, item2)
        o_DN += item1

    if (_BS == 'B'):
        return (o_DN, o_PW)
    elif (_BS == 'S'):
        return (o_DN, -1*o_PW)
    else:
        # if c, default as B
        return (o_DN, o_PW)


def __parse_csv(file_name):
    df = pd.read_csv(file_name)
    columns_name = df.columns.tolist()
    # 00:'CODE'
    # 01:'day'
    # 02:'t'
    # 03:'dn'
    # 04:'dc'
    # 05:'cc'
    # 06:'BS' > B/S/0
    # 07:'DP' > PRICE
    # 08:'DN' > NUMBER
    # 09:'SS' > sequence S
    # 10:'SB' > sequence B
    print("列名：", columns_name)

    START_LINE = 0
    # PRE
    BS_PRE = 0
    DP_PRE = 0
    DN_PRE = 0
    SS_PRE = 0
    SB_PRE = 0

    # current
    SS_ = 0
    SB_ = 0
    UN_INITALIZED = True

    BS_OUTPUT = []
    DP_OUTPUT = []
    DN_OUTPUT = []
    SS_OUTPUT = []
    SB_OUTPUT = []
    PW_OUTPUT = []

    SUM_DN_ARR = []
    SUM_DP_ARR = []

    start_new = False
    # step 1: merge and compute power
    for index, row in df.iterrows():
        row_content = row.tolist()
        if (UN_INITALIZED):
            if (float(row_content[7]) > 0):
                UN_INITALIZED = False
                BS_PRE = row_content[6]
                DP_PRE = float(row_content[7])
                DN_PRE = int(row_content[8])
                SS_PRE = int(row_content[9])
                SB_PRE = int(row_content[10])
                # save INFO
                SUM_DN_ARR.append(DN_PRE)
                SUM_DP_ARR.append(DP_PRE)
        else:
                # merge
                BS_ = row_content[6]
                DP_ = float(row_content[7])
                DN_ = int(row_content[8])

                SS_ = int(row_content[9])
                SB_ = int(row_content[10])

                # if B/S continue, judge merge or not
                if BS_ == BS_PRE:
                    if (BS_ == 'B') and (SB_ == SB_PRE):
                        # B continues, merge
                        SUM_DN_ARR.append(DN_)
                        SUM_DP_ARR.append(DP_)
                    elif (BS_ == 'S') and (SS_ == SS_PRE):
                        # S continues, merge
                        SUM_DN_ARR.append(DN_)
                        SUM_DP_ARR.append(DP_)
                    elif (BS_ == ' '):
                        if (SB_ == 0) and (SB_ == SB_PRE):
                            # ergency B continues, merge
                            SUM_DN_ARR.append(DN_)
                            SUM_DP_ARR.append(DP_)
                        elif (SS_ == 0) and (SS_ == SS_PRE):
                            # ergency S continues, merge
                            SUM_DN_ARR.append(DN_)
                            SUM_DP_ARR.append(DP_)
                        else:
                            start_new = True
                    else:
                        start_new = True
                else:
                    start_new = True

                if (start_new):
                    # revert start_new
                    start_new = False
                    # end the PRE D
                    if ((len(SUM_DN_ARR) + len(SUM_DP_ARR)) > 0):
                        o_DN, o_PW = compute_power_of(SUM_DN_ARR, SUM_DP_ARR, BS_PRE)
                        if (BS_PRE == 'B'):
                            SS_OUTPUT.append(0)
                            SB_OUTPUT.append(SB_PRE)
                            DN_OUTPUT.append(o_DN)
                            PW_OUTPUT.append(o_PW)
                            BS_OUTPUT.append(BS_PRE)
                        elif (BS_PRE == 'S'):
                            SS_OUTPUT.append(SS_PRE)
                            SB_OUTPUT.append(0)
                            DN_OUTPUT.append(o_DN)
                            PW_OUTPUT.append(o_PW)
                            BS_OUTPUT.append(BS_PRE)
                        elif (BS_PRE == ' '):
                            # c, count in b / s
                            # if SS_PRE == 0, it means that ergency S
                            # as S
                            if (SS_PRE == 0):
                                DN_OUTPUT.append(o_DN)
                                PW_OUTPUT.append(-1*o_PW)
                                SS_OUTPUT.append(0)
                                SB_OUTPUT.append(SB_PRE)
                                BS_OUTPUT.append(BS_PRE)
                            # if SB_PRE == 0, it means that ergency B
                            # as B
                            if (SB_PRE == 0):
                                DN_OUTPUT.append(o_DN)
                                PW_OUTPUT.append(o_PW)
                                SS_OUTPUT.append(SS_PRE)
                                SB_OUTPUT.append(0)
                                BS_OUTPUT.append(BS_PRE)

                    SUM_DN_ARR.clear()
                    SUM_DP_ARR.clear()

                    BS_PRE = row_content[6]
                    DP_PRE = float(row_content[7])
                    DN_PRE = int(row_content[8])
                    SS_PRE = int(row_content[9])
                    SB_PRE = int(row_content[10])
                    # save INFO
                    SUM_DN_ARR.append(DN_PRE)
                    SUM_DP_ARR.append(DP_PRE)



    # step 2: sum power
    #data_output = {
    #    "BS": BS_OUTPUT,
    #    "DN": DN_OUTPUT,
    #    "PW": PW_OUTPUT,
    #    "SS": SS_OUTPUT,
    #    "SB": SB_OUTPUT
    #}

    #o_df = pd.DataFrame(data_output)
    #o_df.to_csv('output.csv', index=False)


    sum_DN_B = 0
    sum_DN_S = 0

    sum_power_B = 0.0
    sum_power_S = 0.0

    #for debug
    #max_power_B = 0.0
    #max_DN_B = 0.0
    #max_SB_B = 0.0
    #max_power_B_index = 0
    #count = 0

    for o_BS,o_DN,o_PW,o_SS,oSB  in zip(BS_OUTPUT,DN_OUTPUT,PW_OUTPUT,SS_OUTPUT,SB_OUTPUT):
        print("{:5} {:10} {:25} {:8} {:8}".format(o_BS,o_DN,o_PW,o_SS,oSB))
        if (o_BS == 'B'):
            sum_power_B += o_PW
            sum_DN_B += o_DN
            # for debug
            #if (o_PW > max_power_B):
            #    max_power_B = o_PW
            #    max_DN_B = o_DN
            #    max_SB_B = oSB
            #    max_power_B_index = count
        elif (o_BS == 'S'):
            sum_power_S += o_PW
            sum_DN_S += o_DN
        #else:
        #    sum_power_S += -1*o_PW
        #    sum_power_B += o_PW
        #count += 1

    print("p_b:", sum_power_B, sum_DN_B)
    print("p_s:", sum_power_S, sum_DN_S)
    #print("max power B:", max_power_B, max_DN_B, max_SB_B, max_power_B_index)




# entry
__parse_csv("./deal.csv")
