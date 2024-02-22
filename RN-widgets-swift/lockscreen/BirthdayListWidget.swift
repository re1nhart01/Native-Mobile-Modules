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
struct BirthdayListEntry: TimelineEntry {
    let date: Date
    let list: [Person]
}

struct BirthdayListProvider: TimelineProvider {
    func placeholder(in context: Context) -> BirthdayListEntry {
      BirthdayListEntry(date: Date(), list: [])
    }

    func getSnapshot(in context: Context, completion: @escaping (BirthdayListEntry) -> ()) {
      let entry = BirthdayListEntry(date: Date(), list: [])
      completion(entry)
    }

  func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {

    LockScreenWidgetsProvider.getBirthdays(isList: true) { response in
      let now = Date()
      let arr = response["data"] as? [[String: Any]] ?? []

      let refactoredArray: [Person] = arr.compactMap { dict in
          let name = dict["name"] as! String
          let dateDict = dict["date"] as! [String: Int?]
          if let day = dateDict["day"] as? Int, let month = dateDict["month"] as? Int {
              let year = dateDict["year"] as? Int ?? nil
              let birthdayDate = BirthdayDate(day: day, month: month, year: year)
              return Person(name: name, date: birthdayDate)
          } else {
              return nil
          }
      }
      let entry = BirthdayListEntry(
        date: now,
        list: refactoredArray
      )
      let timeline = Timeline(entries: [entry], policy: .after(now.addingTimeInterval(900)))
      completion(timeline)
    }
  }
}


// UI


struct BirthdayListWidgetView : View {
  var entry: BirthdayListProvider.Entry
  var body: some View {
    ZStack {
      AccessoryWidgetBackground()
      VStack(alignment: .leading, spacing: 0) {
        HStack(alignment: .center, spacing: 0){
          Image("birthday").resizable().frame(width: 12, height: 10)
            .padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 2))
          Text("WEEK BIRTHDAYS").font(.system(size: 9)).foregroundColor(.white).fontWeight(.bold).opacity(0.6)
          Spacer()
        }.padding(EdgeInsets(top: 7, leading: 7, bottom: 0, trailing: 7))
        VStack(alignment: .leading, spacing: 0) {
          if entry.list.count <= 0 {
            Text("There is no birthdays left this week").font(.system(size: 8)).foregroundColor(.white).fontWeight(.bold).padding(.top, 4)
          } else {
            ForEach(Array(entry.list.enumerated()), id: \.offset) { index, item in
              if index < 2 {
                Text("\(item.date.day) \(Months[item.date.month]) - \(item.name)").font(.system(size: 10)).fontWeight(.bold)
              }
            }
          }
          if entry.list.count > 2 {
            Text("+ \(entry.list.count - 2) MORE").font(.system(size: 9)).foregroundColor(.white).fontWeight(.bold).opacity(0.6)
          } else {
            Spacer()
          }
        }.padding(EdgeInsets(top: 0, leading: 7, bottom: 7, trailing: 7))
      }.cornerRadius(12)
    }.ignoresSafeArea().cornerRadius(12)
  }
}


struct BirthdayListWidget: Widget {
    let kind: String = "BirthdaysListWidget"
    let facing: String = "Weekly birthdays"
    let description: String = "Birthdays this week"
    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: BirthdayListProvider()) { entry in
          BirthdayListWidgetView(entry: entry)
        }
        .configurationDisplayName(self.facing)
        .description(self.description)
        .supportedFamilies([.accessoryRectangular])
    }
}
