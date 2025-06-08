//
//  CitationMetadata.swift
//  FirebaseAIBridge
//
//  Created by Sean Chin on 29/5/25.
//
import Foundation
import FirebaseAI

@objcMembers
public class CitationMetadataObjc: NSObject {
    public let citations : [CitationObjc]
    
    public init(citations: [CitationObjc]) {
        self.citations = citations
    }
    
    public static func from(_ citationMetadata: CitationMetadata) -> CitationMetadataObjc {
        return CitationMetadataObjc(citations: citationMetadata.citations.map {
            CitationObjc.from($0)
            }
        )
    }
}
