//
//  GenerateContentResponse.swift
//  FirebaseAIBridge
//
//  Created by Sean Chin on 29/5/25.
//
import Foundation
import FirebaseAI

@objcMembers
public class GenerateContentResponseObjc: NSObject {
    public let candidates: [CandidateObjc]

    public let promptFeedback: PromptFeedbackObjc?

    public let usageMetadata: UsageMetadataObjc?
    
    public init(candidates: [CandidateObjc],
                promptFeedback: PromptFeedbackObjc?,
                usageMetadata: UsageMetadataObjc?) {
        self.candidates = candidates
        self.promptFeedback = promptFeedback
        self.usageMetadata = usageMetadata
    }
    
    
    public static func from(_ response: GenerateContentResponse) -> GenerateContentResponseObjc {
        return GenerateContentResponseObjc(
            candidates: response.candidates.map {
                CandidateObjc.from($0)
            },
            promptFeedback: response.promptFeedback.map {
                PromptFeedbackObjc.from($0)
            },
            usageMetadata: response.usageMetadata.map {
                UsageMetadataObjc.from($0)
            }
        )
    }
}


