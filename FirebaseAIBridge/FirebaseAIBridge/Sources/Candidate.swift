//
//  Candidate.swift
//  FirebaseAIBridge
//
//  Created by Sean Chin on 29/5/25.
//
import Foundation
import FirebaseAI

@objcMembers
public class CandidateObjc: NSObject {
    public let content: ModelContentObjc

    public let safetyRatings: [SafetyRatingObjc]

    public let finishReason: FinishReasonObjc?

    public let citationMetadata: CitationMetadataObjc?
    
    public init(
        content: ModelContentObjc,
        safetyRatings: [SafetyRatingObjc],
        finishReason: FinishReasonObjc?,
        citationMetadata: CitationMetadataObjc?
    ) {
        self.content = content
        self.safetyRatings = safetyRatings
        self.finishReason = finishReason
        self.citationMetadata = citationMetadata
    }
    
    public static func from(_ candidate: Candidate) -> CandidateObjc {
        return CandidateObjc(
            content: ModelContentObjc.from(candidate.content),
            safetyRatings: candidate.safetyRatings.map {
                SafetyRatingObjc.from($0)
            },
            finishReason: candidate.finishReason.map {
                FinishReasonObjc.from($0)
            },
            citationMetadata: candidate.citationMetadata.map {
                CitationMetadataObjc.from($0)
            }
        )
    }
}
