//
//  ContentModality.swift
//  FirebaseAIBridge
//
//  Created by Sean Chin on 30/5/25.
//
import Foundation
import FirebaseAI

@objc public enum ContentModalityObjc: Int {
    case unspecified = 0
    case text = 1
    case image = 2
    case video = 3
    case audio = 4
    case document = 5
    
    public static func from(_ modality: ContentModality) -> ContentModalityObjc {
        switch modality.rawValue {
        case ContentModality.text.rawValue:
            return .text
        case ContentModality.image.rawValue:
            return .image
        case ContentModality.video.rawValue:
            return .video
        case ContentModality.audio.rawValue:
            return .audio
        case ContentModality.document.rawValue:
            return .document
        default:
            return .unspecified
        }
    }
}
