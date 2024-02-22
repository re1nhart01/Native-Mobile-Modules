//
//  BirthdaysWidget.swift
//  LockScreenWidgetsExtension
//
//  Created by Evgeniy Kokaiko on 28.02.2023.
//

import Foundation
import WidgetKit
import SwiftUI


// DATA STORING AND BLL
struct BirthdaysEntry: TimelineEntry {
    let date: Date
    let count: Int
    let period: String
}

struct BirthdayProvider: TimelineProvider {
    func placeholder(in context: Context) -> BirthdaysEntry {
      BirthdaysEntry(date: Date(), count: 0, period: "Today")
    }

    func getSnapshot(in context: Context, completion: @escaping (BirthdaysEntry) -> ()) {
      let entry = BirthdaysEntry(date: Date(), count: 0, period: "Today")
      completion(entry)
    }

  func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {

    LockScreenWidgetsProvider.getBirthdays(isList: false) { response in
      let now = Date()
      let entry = BirthdaysEntry(
        date: now,
        count: response["count"] as? Int ?? 0,
        period: response["period"] as? String ?? "Today"
      )
      let timeline = Timeline(entries: [entry], policy: .after(now.addingTimeInterval(900)))
      completion(timeline)
    }
  }
}


// UI


struct BirthdayWidgetView : View {
  var entry: BirthdayProvider.Entry

    var body: some View {
      ZStack {
        AccessoryWidgetBackground()
        VStack(alignment: .center, spacing: 0, content: {
          Spacer()
          HStack(alignment: .center, spacing: 0, content: {
            Text(String(entry.count)).font(.system(size: 20)).foregroundColor(.white).fontWeight(.medium)
              .padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 3))
            Image("Confetti").resizable().frame(width: 20, height: 20).foregroundColor(.white)
          }).padding(0)
          HStack {
            Text(entry.period).font(.system(size: 9)).foregroundColor(.white).fontWeight(.medium)
          }.padding(EdgeInsets(top: 3, leading: 0, bottom: 0, trailing: 0))
          Spacer()
        }).cornerRadius(100)
      }.ignoresSafeArea()
    }
}


struct BirthdaysWidget: Widget {
    let kind: String = "BirthdaysWidget"
    let facing: String = "Today's birthdays"
    let description: String = "Birthdays for today"
    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: BirthdayProvider()) { entry in
          BirthdayWidgetView(entry: entry)
        }
        .configurationDisplayName(self.facing)
        .description(self.description)
        .supportedFamilies([.accessoryCircular])
    }
}
