Pod::Spec.new do |s|
  s.name             = 'FirebaseAIBridge'
  s.version          = "0.1.0"
  s.summary          = 'Firebase AI Objective-C Bridge'

  s.homepage         = 'https://github.com/SeanChinJunKai'
  s.license          = { :type => 'Apache-2.0', :file => 'LICENSE' }
  s.authors          = 'Sean Chin Jun Kai'

  s.source           = {
    :git => 'https://github.com/SeanChinJunKai/FirebaseAIBridge.git',
    :tag => 'CocoaPods-' + s.version.to_s
  }

  ios_deployment_target = '15.0'

  s.ios.deployment_target = ios_deployment_target

  s.cocoapods_version = '>= 1.12.0'
  s.prefix_header_file = false

  s.source_files = [
    'FirebaseAIBridge/Sources/*.{swift,h}',
  ]
  
  s.public_header_files = [
    'FirebaseAIBridge/Sources/*.h'
  ]

  s.swift_version = '5.9'

  s.framework = 'Foundation'
  s.ios.framework = 'UIKit'

  s.dependency 'FirebaseAI'
end
