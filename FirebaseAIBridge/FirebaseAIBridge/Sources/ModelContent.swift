//
//  ModelContent.swift
//  FirebaseAIBridge
//
//  Created by Sean Chin on 30/5/25.
//
import Foundation
import FirebaseAI

@objcMembers
public class ModelContentObjc: NSObject {
    public let role: String?
    
    public let parts: [PartObjc]
    
    public init(role: String?, parts: [PartObjc]) {
        self.role = role
        self.parts = parts
    }
    
    public static func from(_ modelContent: ModelContent) -> ModelContentObjc {
        return ModelContentObjc(
            role: modelContent.role,
            parts: modelContent.parts.map {
                PartObjc.from($0)
            }
        )
    }
    
    public static func to(_ modelContent: ModelContentObjc) -> ModelContent {
        let internalParts: [any PartsRepresentable] = modelContent.parts.map { part in
            switch part {
                case let part as TextPartObjc:
                    return TextPart(part.text)
                case let part as InlineDataPartObjc:
                    return InlineDataPart(data: part.data, mimeType: part.mimeType)
                case let part as FileDataPartObjc:
                    return FileDataPart(uri: part.fileURI, mimeType: part.mimeType)
                case let part as ImagePartObjc:
                    return part.image
                default:
                    fatalError("Unsupported part type")
            }
        }
        return ModelContent(
            role: modelContent.role,
            parts: internalParts
        )
    }
    
}
