//
//  UserDefaults.swift
//
//  Created by Evgeniy Kokaiko on 20.02.2024.
//

import Foundation
import React

let appGroup = "group.com.appName.state"
//Methods which should be implemented in RN
protocol IUserDefaults {
  func set(_ key: NSString, _ value: NSString, _ suiteName: NSString?, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock)
  func get(_ key: NSString, _ suiteName: NSString?, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock)
  func empty(suiteName: String?, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock)
  func userDefaultsStorage(for suiteName: String?) -> UserDefaults
  func remove(_ key: NSString, _ suiteName: NSString?, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock)
}

@objc(UserDefaultsHandler)
class UserDefaultsHandler : NSObject, IUserDefaults {
  @objc var bridge: RCTBridge!
  @objc var DEFAULT_KEY = "DEFAULT_KEY"
  private static let defaults = UserDefaults.init(suiteName: appGroup)
  
  @objc func set(_ key: NSString, _ value: NSString, _ suiteName: NSString?, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    let resolverDict: NSMutableDictionary = [
      "isError": false,
      "errorMessage": "",
    ]
    do {
      let userDefaults = self.userDefaultsStorage(for: suiteName as String?)
      userDefaults.set((value as String) , forKey: (key as String))
      resolve(resolverDict)
    } catch let error {
      resolverDict["errorMessage"] = error.localizedDescription
      resolve(resolverDict)
    }
  }
  
  @objc func get(_ key: NSString, _ suiteName: NSString?, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    let userDefaults = self.userDefaultsStorage(for: suiteName as String?)
    var data = userDefaults.value(forKey: key as String)
    var result = self.DEFAULT_KEY
    if let intValue = data as? Int {
      result = String(intValue)
    } else if let stringValue = data as? String {
      result = data as! String
    }
    resolve(result as NSString)
  }
  
  @objc func empty(suiteName: String?, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    let userDefaults = self.userDefaultsStorage(for: suiteName as String?)
    userDefaults.removeSuite(named: suiteName ?? "com.app.some")
    resolve(true)
  }
  
  @objc func remove(_ key: NSString, _ suiteName: NSString?, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    do {
      let userDefaults = self.userDefaultsStorage(for: suiteName as String?)
      userDefaults.removeObject(forKey: key as String)
      resolve(true)
    } catch {
      resolve(false)
    }
  }
  
  @objc func userDefaultsStorage(for suiteName: String?) -> UserDefaults {
      if let suiteName = suiteName, !suiteName.isEmpty {
        return UserDefaults(suiteName: suiteName) ?? UserDefaults.standard
      }
      return UserDefaults.standard
  }
  
  
  @objc static func requiresMainQueueSetup() -> Bool {
    return false
  }
  
  
  @objc static func setStatic(_ key: NSString, value: NSNumber?) {
    defaults?.setValue(value?.intValue, forKey: key as String)
  }
  
  @objc static func getStatic(_ key: NSString) -> NSNumber {
    guard let value = defaults?.integer(forKey: key as String) else {
      return 0
    }
    return NSNumber.init(value: value)
  }
  
  
}
