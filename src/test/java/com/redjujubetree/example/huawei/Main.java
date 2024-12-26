package com.redjujubetree.example.huawei;

import java.util.*;
 
public class Main {
 
    // 将扑克牌转换为对应的数字值
    private static int cardValue(String card) {
        switch (card) {
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10": return 10;
            case "J": return 11;
            case "Q": return 12;
            case "K": return 13;
            case "A": return 14;
            default: return 0;  // 忽略 '2'
        }
    }
 
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] inputCards = new String[13];
        
        for (int i = 0; i < 13; i++) {
            inputCards[i] = scanner.next();
        }
        scanner.close();
 
        // 转换并排序卡片，排除 '2'
        List<Integer> cardNumbers = new ArrayList<>();
        for (String card : inputCards) {
            int value = cardValue(card);
            if (value != 0) { // 跳过 '2'
                cardNumbers.add(value);
            }
        }
 
        Collections.sort(cardNumbers);
 
        List<List<Integer>> sequences = new ArrayList<>();
        boolean[] used = new boolean[cardNumbers.size()]; // 记录每张牌是否已被使用
 
        // 查找所有可能的顺子
        for (int i = 0; i < cardNumbers.size(); i++) {
            if (used[i]) continue; // 已使用的牌跳过
 
            List<Integer> currentSequence = new ArrayList<>();
            currentSequence.add(cardNumbers.get(i));
            used[i] = true; // 标记当前牌已使用
            
            for (int j = i + 1; j < cardNumbers.size(); j++) {
                if (used[j]) continue; // 跳过已使用的牌
 
                if (cardNumbers.get(j) == currentSequence.get(currentSequence.size() - 1) + 1) {
                    currentSequence.add(cardNumbers.get(j));
                    used[j] = true; // 标记当前牌已使用
                } else if (cardNumbers.get(j) > currentSequence.get(currentSequence.size() - 1) + 1) {
                    break; // 不再连续，结束当前顺子的查找
                }
            }
 
            if (currentSequence.size() >= 5) {
                sequences.add(new ArrayList<>(currentSequence));
            } else {
                // 如果当前顺子不满足要求，重置已使用标记
                for (Integer num : currentSequence) {
                    used[cardNumbers.indexOf(num)] = false;
                }
            }
        }
 
        // 输出结果
        if (sequences.isEmpty()) {
            System.out.println("No");
        } else {
            for (List<Integer> seq : sequences) {
                for (int i = 0; i < seq.size(); i++) {
                    if (i > 0) System.out.print(" ");
                    int value = seq.get(i);
                    if (value == 11) System.out.print("J");
                    else if (value == 12) System.out.print("Q");
                    else if (value == 13) System.out.print("K");
                    else if (value == 14) System.out.print("A");
                    else System.out.print(value);
                }
                System.out.println();
            }
        }
    }
}