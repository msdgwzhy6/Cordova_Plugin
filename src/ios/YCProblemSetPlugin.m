//
//  YCProblemSetPlugin.h
//  YCMath345-iOS
//
//  Created by jiChengSun on 15/10/20.
//  Copyright © 2015年 jichengsun. All rights reserved.
//

#import "YCProblemSetPlugin.h"

@implementation YCProblemSetPlugin
- (void)loadImage:(CDVInvokedUrlCommand*)command
{
    UIImage *_originImage = [UIImage imageNamed:@"icon@2x.png"];
    
    NSData *_data = UIImageJPEGRepresentation(_originImage, 1.0f);
    
    NSString *_encodedImageStr = [_data base64EncodedStringWithOptions:0];
    
//    DDLogWarn(@"===Encoded image:\n%@", _encodedImageStr);
    
    CDVPluginResult* pluginResult = nil;
    
    if (_encodedImageStr != nil && [_encodedImageStr length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:_encodedImageStr];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)loadProblem:(CDVInvokedUrlCommand*)command{

    NSArray *array = [NSArray arrayWithObjects:@"test001",@"test002",@"test003",@"test004",nil];

    CDVPluginResult* pluginResult = nil;
    
    if (array != nil && [array count] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:array];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

}

@end
