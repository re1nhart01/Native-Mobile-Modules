//
//  ActionsWidget.swift
//  LockScreenWidgetsExtension
//
//  Created by Evgeniy Kokaiko on 28.02.2023.
//

import Foundation
import WidgetKit
import SwiftUI

struct ActionsEntry: TimelineEntry {
    let date: Date
    let count: Int
}

struct ActionsProvider: TimelineProvider {
    func placeholder(in context: Context) -> ActionsEntry {
      ActionsEntry(date: Date(), count: 0)
    }

    func getSnapshot(in context: Context, completion: @escaping (ActionsEntry) -> ()) {
      let entry = ActionsEntry(date: Date(), count: 0)
        completion(entry)
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {
       let now = Date()
       LockScreenWidgetsProvider.getAssistantActions { response in

        let entry = ActionsEntry(
          date: now,
          count: response["count"] as? Int ?? 0
        )
        let timeline = Timeline(entries: [entry], policy: .after(now.addingTimeInterval(900)))
        completion(timeline)
      }
    }
}


struct ActionsWidgetView : View {
  var entry: ActionsProvider.Entry

    var body: some View {
      ZStack {
        AccessoryWidgetBackground()
        VStack(alignment: .center, spacing: 0, content: {
          Spacer()
          HStack(alignment: .center, spacing: 0, content: {
            Text(String(entry.count)).font(.system(size: 20)).foregroundColor(.white).fontWeight(.medium)
              .padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 3))
            Image("assistant").resizable().frame(width: 20, height: 20).foregroundColor(.white)
          }).padding(0)
          HStack {
            Text("ACTIONS").font(.system(size: 8)).foregroundColor(.white).fontWeight(.medium)
          }.padding(EdgeInsets(top: 3, leading: 0, bottom: 0, trailing: 0))
          Spacer()
        }).cornerRadius(100)
      }.ignoresSafeArea()
    }
}

struct ActionsWidget: Widget {
    let kind: String = "ActionsWidget"
    let facing: String = "Assistant actions"
    let description: String = "Assistant actions widget"
    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: ActionsProvider()) { entry in
          ActionsWidgetView(entry: entry)
        }
        .configurationDisplayName(self.facing)
        .description(self.description)
        .supportedFamilies([.accessoryCircular])
    }
}
