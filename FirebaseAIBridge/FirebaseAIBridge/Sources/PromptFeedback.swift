//
//  PromptFeedback.swift
//  FirebaseAIBridge
//
//  Created by Sean  Chin on 29/5/25.
//
import Foundation
import FirebaseAI

@objcMembers
public class PromptFeedbackObjc: NSObject {
    public let blockReason: BlockReasonObjc?
    
    public let blockReasonMessage: String?
    
    public let safetyRatings: [SafetyRatingObjc]
    
    public init(blockReason: BlockReasonObjc?, blockReasonMessage: String?,
                safetyRatings: [SafetyRatingObjc]) {
      self.blockReason = blockReason
      self.blockReasonMessage = blockReasonMessage
      self.safetyRatings = safetyRatings
    }
    
    public static func from(_ promptFeedback: PromptFeedback) -> PromptFeedbackObjc {
        return PromptFeedbackObjc(
            blockReason:  promptFeedback.blockReason.map { BlockReasonObjc.from($0) },
            blockReasonMessage: promptFeedback.blockReasonMessage,
            safetyRatings: promptFeedback.safetyRatings.map { SafetyRatingObjc.from($0) }
        )
    }
}

