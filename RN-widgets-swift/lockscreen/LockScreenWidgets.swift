//
//  LockScreenWidgets.swift
//  LockScreenWidgets
//
//  Created by Evgeniy Kokaiko on 28.02.2023.
//

import WidgetKit
import SwiftUI


@main
struct LockScreenWidgetsBundle: WidgetBundle {
  @WidgetBundleBuilder
    var body: some Widget {
        ActionsWidget()
        BirthdaysWidget()
        BirthdayListWidget()
    }
}
