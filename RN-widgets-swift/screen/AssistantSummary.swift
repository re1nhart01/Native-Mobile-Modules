//
//  AssistantSummary.swift
//  AssistantSummary
//
//  Created by Evgeniy Kokaiko on 24.02.2023.
//

import WidgetKit
import SwiftUI
import Intents
import CoreFoundation


struct WidgetEntry: TimelineEntry {
    let date: Date
    let assistant_actions: Int
    let active_reminders: Int
    let active_birthdays: Int
    let current_holidays: Int
}

struct Provider: TimelineProvider {
  typealias Entry = WidgetEntry
  let fifteenMinInSecond: Double = 900;


  // this method is initial for render
  func placeholder(in context: Context) -> WidgetEntry {
    WidgetEntry(date: Date(),
                assistant_actions: 0,
                active_reminders: 0,
                active_birthdays: 0,
                current_holidays: 0
    )
  }


  // this method is how your widget appears in the widget gallery and needs static mock data to render quickly
  func getSnapshot(in context: Context, completion: @escaping (WidgetEntry) -> ()) {
    let entry = WidgetEntry(date: Date(), assistant_actions: 0, active_reminders: 0, active_birthdays: 0, current_holidays: 0)
    completion(entry)
  }

  // this is updatable method which perform in policy below:
  func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {

    let userId = AssistantSummaryProvider.getUserId(userGroup: "group.com.assistantSummary.appName")

    AssistantSummaryProvider.performHttpRequest(userId: userId) { response in
      let now = Date()
      let entry = WidgetEntry(
        date: now,
        assistant_actions: response["assistant_actions"] as? Int ?? 0,
        active_reminders: response["active_reminders"] as? Int ?? 0,
        active_birthdays: response["active_birthdays"] as? Int ?? 0,
        current_holidays: response["current_holidays"] as? Int ?? 0)
      let timeline = Timeline(entries: [entry], policy: .after(now.addingTimeInterval(fifteenMinInSecond)))
      completion(timeline)
    }

  }
}


struct AssistantSummaryEntryView : View {
    var entry: Provider.Entry

    var body: some View {
    Color(red: 21/255, green: 23/255, blue: 37/255)
      .ignoresSafeArea() // Ignore just for the color
      .overlay(
        GeometryReader{ geometry in
          if geometry.size.width > 200 {
            MediumAssistantSummary(
              assistantValue: String(entry.assistant_actions),
              remindersValue: String(entry.active_reminders),
              birthdaysValue: String(entry.active_birthdays),
              holidaysValue:  String(entry.current_holidays))
          } else {
            SmallAssistantSummary(
              assistantValue: String(entry.assistant_actions),
              remindersValue: String(entry.active_reminders),
              birthdaysValue: String(entry.active_birthdays),
              holidaysValue:  String(entry.current_holidays))
          }
        }
      )
  }
}

struct AssistantSummary_Previews: PreviewProvider {
    static var previews: some View {
      AssistantSummaryEntryView(entry: WidgetEntry(date: Date(),
       assistant_actions: 0,
       active_reminders: 0,
       active_birthdays: 0,
       current_holidays: 0
      )).previewContext(WidgetPreviewContext(family: .systemSmall))
    }
}

@main
struct AssistantSummary: Widget {
    let kind: String = "AssistantSummary"
    let facingName: String = "Assistant Summary"
    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            AssistantSummaryEntryView(entry: entry)
        }
        .configurationDisplayName(facingName)
        .description("Assistant Weekly Summary widget in application")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}
