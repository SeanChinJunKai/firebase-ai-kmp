//
//  FirebaseAIBridge.swift
//  FirebaseAIBridge
//
//  Created by Sean Chin on 20/5/25.
//
import Foundation
import FirebaseAI

@objc(FirebaseObjc)
public class FirebaseObjc: NSObject {
    //  TODO: Adding missing parameters such as firebase app
    @objc public static func ai(backend: GenerativeBackendObjc) -> FirebaseAIObjc {
        return FirebaseAIObjc(backend: backend)
    }
}


@objc(FirebaseAIObjc)
public class FirebaseAIObjc: NSObject {
    private let service: FirebaseAI
    
    init(backend: GenerativeBackendObjc) {
        switch backend {
        case .googleAI:
            self.service = FirebaseAI.firebaseAI(backend: .googleAI())
        case .vertexAI:
            self.service = FirebaseAI.firebaseAI(backend: .vertexAI())
        }
    }
    // TODO: Add missing parameters
    @objc public func generativeModel(modelName: String) -> GenerativeModelObjc {
        return GenerativeModelObjc(model: self.service.generativeModel(modelName: modelName))
    }
}


@objc(GenerativeModelObjc)
public class GenerativeModelObjc: NSObject {
    private let model: GenerativeModel
    
    init(model: GenerativeModel) {
        self.model = model
    }
    
    
    @objc public func generateContent(prompt: String) async throws -> GenerateContentResponseObjc {
        let response = try await model.generateContent(prompt)
        return GenerateContentResponseObjc.from(response)
    }
    
    @objc public func generateContent(content: [ModelContentObjc]) async throws -> GenerateContentResponseObjc {
        let contents = content.map { ModelContentObjc.to($0) }
        let response = try await model.generateContent(contents)
        return GenerateContentResponseObjc.from(response)
    }
    
    @objc public func generateContentStream(
        prompt: String,
        onResponse: @escaping (GenerateContentResponseObjc) -> Void,
        onComplete: @escaping (NSError?) -> Void
    ) {
        do {
            let responseStream = try model.generateContentStream(prompt)
            Task {
                do {
                    for try await response in responseStream {
                        onResponse(GenerateContentResponseObjc.from(response))
                    }
                    onComplete(nil)
                } catch {
                    onComplete(error as NSError)
                }
            }
        } catch {
            onComplete(error as NSError)
        }
    }
    
    @objc public func generateContentStream(
        content: [ModelContentObjc],
        onResponse: @escaping (GenerateContentResponseObjc) -> Void,
        onComplete: @escaping (NSError?) -> Void
    ) {
        do {
            let contents = content.map { ModelContentObjc.to($0) }
            let responseStream = try model.generateContentStream(contents)
            Task {
                do {
                    for try await response in responseStream {
                        onResponse(GenerateContentResponseObjc.from(response))
                    }
                    onComplete(nil)
                } catch {
                    onComplete(error as NSError)
                }
            }
        } catch {
            onComplete(error as NSError)
        }
    }
    
    
    
    @objc public func countTokens(prompt: String) async throws -> CountTokensResponseObjc {
        let response = try await model.countTokens(prompt)
        return CountTokensResponseObjc.from(response)
    }
    
    @objc public func countTokens(content: [ModelContentObjc]) async throws -> CountTokensResponseObjc {
        let contents = content.map { ModelContentObjc.to($0) }
        let response = try await model.countTokens(contents)
        return CountTokensResponseObjc.from(response)
    }
}
