//
//  InfoRow.swift
//  AssistantSummaryExtension
//
//  Created by Evgeniy Kokaiko on 26.02.2023.
//

import SwiftUI

struct InfoRow: View {
  let label: String
  let value: String
  let icon: String
    var body: some View {
      HStack() {
        Image(icon).resizable().frame(width: 16, height: 16)
          Text(label)
          .font(.system(size: 11))
          .foregroundColor(.white)
          Spacer()
          Text(value)
          .font(.system(size: 11))
          .bold()
          .foregroundColor(Color(red: 144/255, green: 154/255, blue: 176/255))
      }
    }
}
