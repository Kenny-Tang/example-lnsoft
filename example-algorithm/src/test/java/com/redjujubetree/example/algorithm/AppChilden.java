package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.Scanner;

public class AppChilden {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int appCount = Integer.parseInt(scanner.nextLine());
		ArrayList<App> apps = new ArrayList<>();
		int i = 0;
		while (i < appCount) {
			i++;
			App app = new App(scanner.nextLine());
			boolean canRegister = true;
			for (App a : apps) {
				if (!couldRegister(a, app)) {
					canRegister = false;
					break;
				}
			}
			// 如果可以注册讲没有冲突的应用保留
			if (canRegister) {
				ArrayList<App> temp = new ArrayList<>();
				temp.add(app);
				for (App a : apps) {
					if (noInser(a, app)) {
						temp.add(a);
					}
				}
				apps = temp;
			}
		}
		String times[] = scanner.nextLine().split(":");
		int time = Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);

		// 查找该时间段内可以使用的App
		for (App app : apps) {
			if (app.start <= time && app.end > time) {
				System.out.println(app.name);
				return;
			}
		}
		System.out.println("NA");
	}

	// 判断两段时间是否有交集
	public static boolean noInser(App app1, App app2) {
		// 最早的结束时间 早于最晚的开始时间 此种情况无交集
		return Math.max(app1.start, app2.start) >= Math.min(app1.end, app2.end);
	}

	// 判断两段时间是否有冲突
	public static boolean couldRegister(App app1, App app2) {
		// 两段时间没有交集
		boolean result = noInser(app1, app2);
		System.out.println(app1.name + " " + app2.name + " " + result);
		if (result) {
			return true;
		}
		return app1.property < app2.property;
	}

	public static class App{
		public String name;
		public int property;
		public int start;
		public int end;
		public App(String app) {
			String[] ss = app.split(" ");
			name = ss[0];
			property = Integer.parseInt(ss[1]);
			String[] starts = ss[2].split(":");
			start = Integer.parseInt(starts[0]) * 60 + Integer.parseInt(starts[1]);
			String ends[] = ss[3].split(":");
			end = Integer.parseInt(ends[0]) * 60 + Integer.parseInt(ends[1]);
		}
	}
}
