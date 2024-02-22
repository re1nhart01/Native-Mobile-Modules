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

bool facebook::react::NativeSampleModule::createFile(jsi::Runtime& rt, const std::string fName, const std::string content) {
  try {
    std::ofstream NewFile(fName);
    NewFile << content;
    NewFile.close();
    return true;
  } catch (const std::exception& e) {
    return false;
  }
}

std::string facebook::react::NativeSampleModule::getFile(jsi::Runtime& rt, const std::string fName) {
 std::string myText;
 std::ifstream MyReadFile(fName);
 std::string result = "";
 while (getline (MyReadFile, myText)) {
   result += myText;
 }

 MyReadFile.close();
 return result;
}


} // namespace facebook::react