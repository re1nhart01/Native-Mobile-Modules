//
//  UserDefaults.m
//
//  Created by Evgeniy Kokaiko on 20.02.2024.
//

#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"

@interface RCT_EXTERN_MODULE(UserDefaultsHandler, NSObject)

RCT_EXTERN_METHOD(set :(NSString *)key
                  :(NSString *) value
                  :(NSString *) suiteName
                  resolver:(RCTPromiseResolveBlock) resolve
                  rejecter :(RCTPromiseRejectBlock) reject)

RCT_EXTERN_METHOD(get :(NSString *)key
                  :(NSString *)suiteName
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter :(RCTPromiseRejectBlock) reject)

RCT_EXTERN_METHOD(empty :(NSString *)suiteName
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter :(RCTPromiseRejectBlock) reject)

RCT_EXTERN_METHOD(remove :(NSString *)key
                  :(NSString *)suiteName
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter :(RCTPromiseRejectBlock) reject)
@end
