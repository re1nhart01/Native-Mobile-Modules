#include "NativeSampleModule.h"
#include <cstdlib>

namespace facebook::react {

NativeSampleModule::NativeSampleModule(std::shared_ptr<CallInvoker> jsInvoker)
    : NativeSampleModuleCxxSpec(std::move(jsInvoker)) {}

std::string NativeSampleModule::reverseString(jsi::Runtime& rt, std::string input) {
  return std::string(input.rbegin(), input.rend());
}

std::string NativeSampleModule::getRand(jsi::Runtime& rt) {
  srand((unsigned) time(NULL));
  int random = arc4random();
  return std::to_string(random);
}



} // namespace facebook::react