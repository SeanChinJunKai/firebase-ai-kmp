//
//  ModalityTokenCount.swift
//  FirebaseAIBridge
//
//  Created by Sean Chin on 30/5/25.
//
import Foundation
import FirebaseAI

@objcMembers
public class ModalityTokenCountObjc: NSObject {
    public let modality: ContentModalityObjc
    
    public let tokenCount: Int
    
    public init(modality: ContentModalityObjc, tokenCount: Int) {
        self.modality = modality
        self.tokenCount = tokenCount
    }
    
    public static func from(_ modalityTokenCount: ModalityTokenCount) -> ModalityTokenCountObjc {
        return ModalityTokenCountObjc(
            modality: ContentModalityObjc.from(modalityTokenCount.modality),
            tokenCount: modalityTokenCount.tokenCount
        )
    }
}
