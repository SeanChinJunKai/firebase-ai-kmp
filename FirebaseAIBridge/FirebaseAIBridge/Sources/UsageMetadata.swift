//
//  UsageMetadata.swift
//  FirebaseAIBridge
//
//  Created by Sean Chin on 29/5/25.
//
import Foundation
import FirebaseAI

@objcMembers
public class UsageMetadataObjc: NSObject {
    public let promptTokenCount: Int

    public let candidatesTokenCount: Int

    public let totalTokenCount: Int

    public let promptTokensDetails: [ModalityTokenCountObjc]


    public let candidatesTokensDetails: [ModalityTokenCountObjc]
    
    public init(promptTokenCount: Int,
                candidatesTokenCount: Int,
                totalTokenCount: Int,
                promptTokensDetails: [ModalityTokenCountObjc],
                candidatesTokensDetails: [ModalityTokenCountObjc]) {
        self.promptTokenCount = promptTokenCount
        self.candidatesTokenCount = candidatesTokenCount
        self.totalTokenCount = totalTokenCount
        self.promptTokensDetails = promptTokensDetails
        self.candidatesTokensDetails = candidatesTokensDetails
    }
    
    public static func from(_ usageMetadata: GenerateContentResponse.UsageMetadata) -> UsageMetadataObjc {
        return UsageMetadataObjc(
            promptTokenCount: usageMetadata.promptTokenCount,
            candidatesTokenCount: usageMetadata.candidatesTokenCount,
            totalTokenCount: usageMetadata.totalTokenCount,
            promptTokensDetails: usageMetadata.promptTokensDetails.map {
                ModalityTokenCountObjc.from($0)
            },
            candidatesTokensDetails:usageMetadata.candidatesTokensDetails.map {
                ModalityTokenCountObjc.from($0)
            }
        )
    }
}
