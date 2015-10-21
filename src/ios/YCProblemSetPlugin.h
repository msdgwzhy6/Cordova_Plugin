//
//  YCProblemSetCDVPlugin.h
//  YCMath345-iOS
//
//  Created by jiChengSun on 15/10/20.
//  Copyright © 2015年 jichengsun. All rights reserved.
//

#import "CDVPlugin.h"

@interface YCProblemSetPlugin : CDVPlugin
- (void)loadImage:(CDVInvokedUrlCommand*)command;

-(void)loadProblem:(CDVInvokedUrlCommand*)command;
@end
