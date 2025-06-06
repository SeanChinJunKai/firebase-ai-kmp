//
//  FinishReason.swift
//  FirebaseAIBridge
//
//  Created by Sean Chin on 30/5/25.
//
import Foundation
import FirebaseAI

@objcMembers
public class FinishReasonObjc: NSObject {
    public let rawValue: String
    
    public init(rawValue: String) {
        self.rawValue = rawValue
    }
    public static func from(_ finishReason: FinishReason) -> FinishReasonObjc {
        return FinishReasonObjc(rawValue: finishReason.rawValue)
    }
}
