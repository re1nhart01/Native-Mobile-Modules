//
//  AssistantSummaryProvider.swift
//  AssistantSummaryExtension
//
//  Created by Evgeniy Kokaiko on 27.02.2023.
//

import Foundation

enum AssistantSummaryProviderResponse {
    case Success([String: Any])
    case Failure
}

struct AssistantSummaryResponse: Decodable {
  var assistant_actions: Int
  var active_reminders: Int
  var active_birthdays: Int
  var current_holidays: Int
}


class AssistantSummaryProvider {
  static var serverURL: String = "API_URL"

  static func performHttpRequest(userId: String?, completion: @escaping ([String: Any]) -> Void) {
      print("Start performing http request")
      let url = URL(string: AssistantSummaryProvider.serverURL + "/widgets/summary")!
      var request = URLRequest(url: url)
      request.httpMethod = "POST"
      request.addValue("application/json", forHTTPHeaderField: "Content-Type")
      let normalizedUserId = userId?.replacingOccurrences(of: "\"", with: "")
      let body = ["userId": normalizedUserId]
      let jsonData = try? JSONSerialization.data(withJSONObject: body)
      request.httpBody = jsonData

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
  static func getUserId(userGroup: String) -> String? {
    guard var userId = UserDefaults.init(suiteName: userGroup)?.value(forKey: "userId") as? String else {
      return ""
    }
    return userId
  }
}
