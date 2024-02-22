//
//  LockScreenProvider.swift
//  LockScreenWidgetsExtension
//
//  Created by Evgeniy Kokaiko on 28.02.2023.
//

import Foundation


enum AssistantSummaryProviderResponse {
    case Success([String: Any])
    case Failure
}

struct AssistantSummaryResponse: Decodable {
  var count: Int
}

struct Person: Codable {
    let name: String
    let date: BirthdayDate
}

struct BirthdayDate: Codable {
    let day: Int
    let month: Int
    let year: Int?
}

public let Months: [String] = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]


class LockScreenWidgetsProvider {
  static var serverURL: String = "URL"


  static func fetch(pathF: String, method: String, headers: [String: String], body: [String: Any], completion: @escaping ([String: Any]) -> Void) -> Void {
    let url = URL(string: LockScreenWidgetsProvider.serverURL + pathF)!
    let jsonData = try? JSONSerialization.data(withJSONObject: body)
    var request = URLRequest(url: url)
    request.httpMethod = method
    request.httpBody = jsonData
    for (key, value) in headers {
      request.addValue(value, forHTTPHeaderField: key)
    }
    URLSession.shared.dataTask(with: request) { data, response, error in
      if let error = error {
          print(error.localizedDescription)
          completion([:])
          return
      }

      if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200 {
          print("HTTP Error \(httpResponse.statusCode)")
          completion([:])
          return
      }

      guard let data = data else {
          print("No data returned")
          completion([:])
          return
      }

      do {
          let response = try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] ?? [:]
          completion(response)
      } catch {
          print("Error parsing JSON: \(error)")
          completion([:])
      }
    }.resume()
  }


  static func getBirthdays(isList: Bool, completion: @escaping ([String: Any]) -> Void) -> Void {
    let headers = ["Content-Type": "application/json"]
    let userId = LockScreenWidgetsProvider.getUserId(userGroup: "group.com.assistantSummary.appName").replacingOccurrences(of: "\"", with: "")

    let body: [String : Any] = [
      "userId": userId,
      "isList": isList
    ]
    LockScreenWidgetsProvider.fetch(pathF: "/widgets/birthdays", method: "POST", headers: headers, body: body, completion: completion)
  }

  static func getAssistantActions(completion: @escaping ([String: Any]) -> Void) -> Void {
    let headers = ["Content-Type": "application/json"]
    let userId = LockScreenWidgetsProvider.getUserId(userGroup: "group.com.assistantSummary.appName").replacingOccurrences(of: "\"", with: "")

    let body = ["userId": userId]
    LockScreenWidgetsProvider.fetch(pathF: "/widgets/actions", method: "POST", headers: headers, body: body, completion: completion)
  }

  static func getUserId(userGroup: String) -> String {
    guard let userId = UserDefaults.init(suiteName: userGroup)?.value(forKey: "userId") as? String else {
      return ""
    }
    return userId
  }
}

