//
//  WidgetSizings.swift
//  AssistantSummaryExtension
//
//  Created by Evgeniy Kokaiko on 26.02.2023.
//

import SwiftUI

struct SmallAssistantSummary: View {
  let assistantValue: String
  let remindersValue: String
  let birthdaysValue: String
  let holidaysValue: String
    var body: some View {
      VStack{
        HStack(alignment: .top) {
          Image("logo").resizable().frame(width: 13, height: 13)
            .padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
          Text("THIS WEEK").font(.system(size: 13)).foregroundColor(Color(red: 144/255, green: 154/255, blue: 176/255)).fontWeight(.bold)
          Spacer()
        }.padding(EdgeInsets(top: 0, leading: 0, bottom: 4, trailing: 0))
      InfoRow(label: "Assistant", value: assistantValue, icon: "assistant_assistant_icon")
      InfoRow(label: "Reminders", value: remindersValue, icon: "assistant_reminders_icon")
      InfoRow(label: "Birthdays", value: birthdaysValue, icon: "assistant_birthdays_icon")
      InfoRow(label: "Holidays", value: holidaysValue, icon: "assitant_holidays_icon")
      }.padding(16)
    }
}

struct MediumAssistantSummary: View {
  let assistantValue: String
  let remindersValue: String
  let birthdaysValue: String
  let holidaysValue: String
    var body: some View {
      HStack {
        VStack {
          HStack{
            Image("logo").resizable().frame(width: 13, height: 13)
              .padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
            Text("THIS WEEK").font(.system(size: 13)).foregroundColor(Color(red: 144/255, green: 154/255, blue: 176/255)).fontWeight(.bold)
            Spacer()
          }.padding(EdgeInsets(top: 0, leading: 0, bottom: 4, trailing: 0))
          InfoRow(label: "Assistant", value: assistantValue, icon: "assistant_assistant_icon")
          InfoRow(label: "Reminders", value: remindersValue, icon: "assistant_reminders_icon")
          InfoRow(label: "Birthdays", value: birthdaysValue, icon: "assistant_birthdays_icon")
          InfoRow(label: "Holidays", value: holidaysValue, icon: "assitant_holidays_icon")
        }.padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 44))
        Image("parrot_image").resizable().frame(width: 130, height: 123)
      }.padding(16)
    }
}


